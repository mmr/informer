package org.b1n.informer.receiver;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ezmorph.MorphException;
import net.sf.json.JSONSerializer;

import org.apache.commons.beanutils.DynaBean;
import org.b1n.framework.persistence.DaoLocator;
import org.b1n.framework.persistence.EntityNotFoundException;
import org.b1n.informer.core.domain.Build;
import org.b1n.informer.core.domain.Host;
import org.b1n.informer.core.domain.HostDao;
import org.b1n.informer.core.domain.ModuleBuild;
import org.b1n.informer.core.domain.Project;
import org.b1n.informer.core.domain.ProjectBuild;
import org.b1n.informer.core.domain.ProjectDao;
import org.b1n.informer.core.domain.User;
import org.b1n.informer.core.domain.UserDao;

/**
 * Salva dados.
 * @author Marcio Ribeiro
 * @date Jan 20, 2008
 */
public class SaveInfoServlet extends HttpServlet {
    private static final String PARAM_PROJECT_NAME = "projectName";
    private static final String PARAM_VERSION = "version";
    private static final String PARAM_GROUP_ID = "groupId";
    private static final String PARAM_ARTIFACT_ID = "artifactId";
    private static final String PARAM_HOSTNAME = "hostName";
    private static final String PARAM_HOSTIP = "hostIp";
    private static final String PARAM_USERNAME = "userName";
    private static final String PARAM_JVM = "jvm";
    private static final String PARAM_ENCODING = "encoding";
    private static final String PARAM_OPERATING_SYSTEM = "operatingSystem";
    private static final String PARAM_MASTER_PROJECT = "masterProject";
    private static final String PARAM_BUILD_INFO = "buildInfo";
    private static final String PARAM_MODULES = "modules";
    private static final String PARAM_BUILD_TIME = "buildTime";
    private static final String PARAM_WITH_TESTS = "withTests";
    private static final String PARAM_DEPLOY = "deploy";

    /**
     * @param req requisicao.
     * @param resp resposta.
     * @throws ServletException caso algo de inesperado ocorra.
     * @throws IOException caso algo de inesperado ocorra.
     */
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    /**
     * Salva dados.
     * @param req requisicao.
     * @param resp resposta.
     * @throws ServletException caso algo de inesperado ocorra.
     * @throws IOException caso algo de inesperado ocorra.
     */
    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String buildInfo = req.getParameter(PARAM_BUILD_INFO);
        final DynaBean data = (DynaBean) JSONSerializer.toJava(JSONSerializer.toJSON(buildInfo));

        if (data == null) {
            throw new ServletException("Requisicao invalida.");
        }

        final ProjectBuild projectBuild = createProjectBuild(data);
        createModulesBuild(data, projectBuild);
    }

    /**
     * Cria build de modulos.
     * @param data dados.
     * @param projectBuild projeto pai.
     */
    @SuppressWarnings("unchecked")
    private void createModulesBuild(final DynaBean data, final ProjectBuild projectBuild) {
        try {
            final List<DynaBean> modules = (List<DynaBean>) data.get(PARAM_MODULES);
            for (final DynaBean module : modules) {
                final String groupId = (String) module.get(PARAM_GROUP_ID);
                final String artifactId = (String) module.get(PARAM_ARTIFACT_ID);
                final String version = (String) module.get(PARAM_VERSION);
                final String projectName = (String) module.get(PARAM_PROJECT_NAME);

                final Project project = getProject(projectName, version, groupId, artifactId);

                final ModuleBuild moduleBuild = new ModuleBuild();
                moduleBuild.setProjectBuild(projectBuild);
                moduleBuild.setProject(project);

                // Parametros comuns
                setCommonParams(module, moduleBuild);

                // Adiciona modulo
                projectBuild.addModule(moduleBuild);
            }
            projectBuild.save();
        } catch (final MorphException e) {
            // Nao tem filhos, tudo bem
        }
    }

    /**
     * Metodo auxiliar que define parametros comuns.
     * @param buildDynaBean dyna bean de build.
     * @param build o build em si.
     */
    private void setCommonParams(final DynaBean buildDynaBean, final Build build) {
        // With tests
        build.setWithTests((Boolean) buildDynaBean.get(PARAM_WITH_TESTS));

        // Deploy
        build.setDeploy((Boolean) buildDynaBean.get(PARAM_DEPLOY));

        // Start & End Time
        final int buildTime = (Integer) buildDynaBean.get(PARAM_BUILD_TIME);
        final Date endTime = new Date();
        final Date startTime = new Date(endTime.getTime() - buildTime);
        build.setStartTime(startTime);
        build.setEndTime(endTime);
    }

    /**
     * Cria build de projeto pai com dados da requisicao.
     * @param data dados.
     * @return build de projeto pai criado.
     */
    private ProjectBuild createProjectBuild(final DynaBean data) {
        // TODO (mmr) : trocar esse monte de binding com nome feio para um esquema de binding
        // automatico (colocar dominio em ponto comum entre Informer e Receiver)
        final DynaBean masterProject = (DynaBean) data.get(PARAM_MASTER_PROJECT);

        // Host
        final String hostName = (String) masterProject.get(PARAM_HOSTNAME);
        final String hostIp = (String) masterProject.get(PARAM_HOSTIP);
        final String operatingSystem = (String) masterProject.get(PARAM_OPERATING_SYSTEM);
        final String jvm = (String) masterProject.get(PARAM_JVM);
        final String encoding = (String) masterProject.get(PARAM_ENCODING);

        // User
        final String userName = (String) masterProject.get(PARAM_USERNAME);

        // Project
        final String projectName = (String) masterProject.get(PARAM_PROJECT_NAME);
        final String artifactId = (String) masterProject.get(PARAM_ARTIFACT_ID);
        final String groupId = (String) masterProject.get(PARAM_GROUP_ID);
        final String version = (String) masterProject.get(PARAM_VERSION);

        final ProjectBuild projectBuild = new ProjectBuild();

        final User user = getUser(userName);
        projectBuild.setUser(user);

        final Host host = getHost(hostName, hostIp, jvm, encoding, operatingSystem);
        projectBuild.setHost(host);

        final Project project = getProject(projectName, version, groupId, artifactId);
        projectBuild.setProject(project);

        // Parametros comuns
        setCommonParams(masterProject, projectBuild);

        // Save
        projectBuild.save();
        return projectBuild;
    }

    /**
     * Metodo auxiliar que cria/encontra host com dados passados.
     * @param hostName nome do host.
     * @param hostIp ip do host.
     * @param jvm java virtual machine do host.
     * @param encoding encoding.
     * @param operatingSystem sistema operacional.
     * @return host criado/encontrado.
     */
    private Host getHost(final String hostName, final String hostIp, final String jvm, final String encoding, final String operatingSystem) {
        final HostDao hostDao = DaoLocator.getDao(Host.class);
        Host host = null;
        try {
            host = hostDao.findByHostName(hostName);
        } catch (final EntityNotFoundException e) {
            host = new Host(hostName, hostIp, operatingSystem, jvm, encoding);
            host.save();
        }
        return host;
    }

    /**
     * Metodo auxiliar que cria/encontra projeto com dados passados.
     * @param projectName nome do projeto.
     * @param version versao.
     * @param groupId id do grupo do artefato.
     * @param artifactId id do artefato.
     * @return projeto criado/encontrado.
     */
    private Project getProject(final String projectName, final String version, final String groupId, final String artifactId) {
        Project project = null;
        try {
            final ProjectDao projectDao = DaoLocator.getDao(Project.class);
            project = projectDao.findByKey(groupId, artifactId, version);
        } catch (final EntityNotFoundException e) {
            project = new Project(groupId, artifactId, version, projectName);
            project.save();
        }
        return project;
    }

    /**
     * Metodo auxiliar que cria/encontra usuario com dados passados.
     * @param userName nome do usuario.
     * @return usuario criado/encontrado.
     */
    private User getUser(final String userName) {
        User user = null;
        try {
            final UserDao userDao = DaoLocator.getDao(User.class);
            user = userDao.findByUserName(userName);
        } catch (final EntityNotFoundException e) {
            user = new User(userName);
            user.save();
        }
        return user;
    }
}
