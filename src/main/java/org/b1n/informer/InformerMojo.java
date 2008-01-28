package org.b1n.informer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.b1n.informer.ds.CouldNotSendDataException;
import org.b1n.informer.ds.DataSender;

/**
 * The Informer.
 * @author Marcio Ribeiro
 * @date Jan 19, 2008
 * @goal informer
 */
public class InformerMojo extends AbstractMojo {

    /** Ultimo projeto. */
    private static MavenProject lastProject;

    /** Mantem dados sobre modulos para mandar de uma vez no final. */
    private static final Map<String, ModuleInfo> modules = new HashMap<String, ModuleInfo>();

    /** Dados sobre o projeto pai. */
    private static MasterProjectInfo masterProjectInfo;

    /** Action para inicio. */
    private static final String START_ACTION = "start";

    /** Action para fim. */
    private static final String END_ACTION = "end";

    /**
     * @parameter expression="${session}"
     * @required
     * @readonly
     */
    private MavenSession session;

    /**
     * O projeto que executou o plugin.
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * <code>true</code> se usuario tiver usado opcao -o (offline mode), false se nao.
     * @parameter default-value="${settings.offline}"
     * @required
     * @readonly
     */
    private boolean offline = false;

    /**
     * Projeto corrente e filhos.
     * @parameter expression="${reactorProjects}"
     * @readonly
     */
    private List<MavenProject> reactorProjects;

    /**
     * URL para o servidor.
     * @parameter
     * @required
     */
    private String server;

    /**
     * A acao.
     * @parameter
     * @required
     */
    private String action;

    /**
     * Classe do data sender.
     * @parameter
     * @required
     */
    private String dataSenderClassName;

    /** Cara que envia os dados. */
    private DataSender dataSender;

    /**
     * Let the magic begin.
     * @throws MojoExecutionException erro.
     * @throws MojoFailureException falha.
     */
    @SuppressWarnings("unchecked")
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (offline) {
            return;
        }

        // Inicia build
        if (project.isExecutionRoot()) {
            if (action.equals(START_ACTION)) {
                lastProject = reactorProjects.get(reactorProjects.size() - 1);
                startTimeMasterProject();
            } else if (action.equals(END_ACTION) && lastProject.equals(project)) {
                // Projeto sem filhos
                endTimeMasterProject();
                sendBuildInfo();
            }
            return;
        }

        // Cuida de tempos de projetos filhos
        if (action.equals(START_ACTION)) {
            startTimeModule();
        } else if (action.equals(END_ACTION)) {
            endTimeModule();
        }

        // Se for ultimo projeto, deve terminar build
        if (project.equals(lastProject) && action.equals(END_ACTION)) {
            endTimeMasterProject();
            sendBuildInfo();
        }
    }

    private void startTimeMasterProject() {
        masterProjectInfo = new MasterProjectInfo(project, session.getStartTime());
    }

    private void endTimeMasterProject() {
        masterProjectInfo.setEndTime(new Date());
    }

    private void startTimeModule() {
        modules.put(project.getId(), new ModuleInfo(project));
    }

    private void endTimeModule() {
        modules.get(project.getId()).setEndTime(new Date());
    }

    /**
     * Envia mensagem de fim de build.
     */
    private void sendBuildInfo() {
        try {
            DataSender ds = getDataSender();

            JSONObject json = new JSONObject();
            json.put("masterProject", masterProjectInfo);
            if (!modules.isEmpty()) {
                json.put("modules", modules.values());
            }
            ds.sendData(JSONSerializer.toJSON(json).toString());
        } catch (CouldNotSendDataException e) {
            getLog().debug(e.getCause());
        }
    }

    /**
     * Cria data sender.
     * @return instancia de data sender.
     * @throws CouldNotSendDataException caso nao consiga enviar dados.
     */
    @SuppressWarnings("unchecked")
    private DataSender getDataSender() throws CouldNotSendDataException {
        if (dataSender != null) {
            return dataSender;
        }
        try {
            Class<DataSender> dsClass = (Class<DataSender>) Class.forName(this.dataSenderClassName);
            Constructor<DataSender> constructor = dsClass.getDeclaredConstructor(new Class[] { String.class });
            return constructor.newInstance(this.server);
        } catch (NoSuchMethodException e) {
            throw new CouldNotSendDataException(e);
        } catch (InstantiationException e) {
            throw new CouldNotSendDataException(e);
        } catch (IllegalAccessException e) {
            throw new CouldNotSendDataException(e);
        } catch (InvocationTargetException e) {
            throw new CouldNotSendDataException(e);
        } catch (ClassNotFoundException e) {
            throw new CouldNotSendDataException(e);
        }
    }
}
