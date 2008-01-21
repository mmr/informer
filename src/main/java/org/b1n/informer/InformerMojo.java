package org.b1n.informer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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
    private static MavenProject lastModule;

    /** Id do build. */
    private static long buildId;

    /** Id do modulo. */
    private static long moduleId;

    /** Logger. */
    private static final Logger LOG = Logger.getLogger(InformerMojo.class);

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
            if (action.equals(DataSender.START_ACTION)) {
                lastModule = reactorProjects.get(reactorProjects.size() - 1);
                sendStartBuild();
            } else if (action.equals(DataSender.END_ACTION) && lastModule == null) {
                // Projeto sem filhos
                sendEndBuild();
            }
            return;
        }

        // Envia requests de modulos
        if (action.equals(DataSender.START_ACTION)) {
            sendStartModule();
        } else if (action.equals(DataSender.END_ACTION)) {
            sendEndModule();
        }

        // Se for ultimo modulo, deve terminar build
        if (project.equals(lastModule) && action.equals(DataSender.END_ACTION)) {
            sendEndBuild();
        }
    }

    /**
     * Envia mensagem de inicio de modulo.
     */
    private void sendStartModule() {
        try {
            DataSender ds = getDataSender();
            moduleId = ds.sendStartModule(getStartModuleData());
        } catch (CouldNotSendDataException e) {
            LOG.debug(e.getCause());
        }
    }

    /**
     * Envia mensagem de fim de modulo.
     */
    private void sendEndModule() {
        try {
            DataSender ds = getDataSender();
            ds.sendEndModule(getEndModuleData());
        } catch (CouldNotSendDataException e) {
            LOG.debug(e.getCause());
        }
    }

    /**
     * Envia mensagem de inicio de build.
     */
    private void sendStartBuild() {
        try {
            DataSender ds = getDataSender();
            buildId = ds.sendStartBuild(getStartBuildData());
        } catch (CouldNotSendDataException e) {
            LOG.debug(e.getCause());
        }
    }

    /**
     * Envia mensagem de fim de build.
     */
    private void sendEndBuild() {
        try {
            DataSender ds = getDataSender();
            ds.sendEndBuild(getEndBuildData());
        } catch (CouldNotSendDataException e) {
            LOG.debug(e.getCause());
        }
    }

    /**
     * @return dados para mensagem de inicio de build.
     */
    private Map<String, String> getStartBuildData() {
        InfoRetriever info = new InfoRetriever();
        Map<String, String> data = new HashMap<String, String>();
        data.put("action", DataSender.START_BUILD_ACTION);
        data.put("project", project.getName());
        data.put("version", project.getVersion());
        data.put("groupId", project.getGroupId());
        data.put("artifactId", project.getArtifactId());
        data.put("hostName", info.getHostName());
        data.put("hostIp", info.getHostIp());
        data.put("userName", info.getUsername());
        data.put("jvm", info.getJvm());
        data.put("encoding", info.getFileEncoding());
        return data;
    }

    /**
     * @return dados para mensagem de inicio de modulo.
     */
    private Map<String, String> getStartModuleData() {
        Map<String, String> data = new HashMap<String, String>();
        data.put("action", DataSender.START_MODULE_ACTION);
        data.put("groupId", project.getGroupId());
        data.put("artifactId", project.getArtifactId());
        data.put("version", project.getVersion());
        data.put("buildId", String.valueOf(buildId));
        return data;
    }

    /**
     * @return dados para mensagem de fim de build.
     */
    private Map<String, String> getEndBuildData() {
        Map<String, String> data = new HashMap<String, String>();
        data.put("action", DataSender.END_BUILD_ACTION);
        data.put("buildId", String.valueOf(buildId));
        return data;
    }

    /**
     * @return dados para mensagem de fim de modulo.
     */
    private Map<String, String> getEndModuleData() {
        Map<String, String> data = new HashMap<String, String>();
        data.put("action", DataSender.END_MODULE_ACTION);
        data.put("moduleId", String.valueOf(moduleId));
        return data;
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
