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
    /** Last module. */
    private static MavenProject lastModule;

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
    private String dataSender;

    /** Logger. */
    private static final Logger LOG = Logger.getLogger(InformerMojo.class);

    /** End action. */
    private static final String END_ACTION = "end";

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

        // Gambiarra para funcionar em projetos modulares
        if (project.isExecutionRoot()) {
            if (lastModule == null) {
                lastModule = reactorProjects.get(reactorProjects.size() - 1);
            } else if (!project.equals(lastModule)) {
                return;
            }
        } else {
            if (project.equals(lastModule) && action.equals(END_ACTION)) {
                project = reactorProjects.get(0);
            } else {
                return;
            }
        }

        // Empacotando dados para data sender
        InfoRetriever info = new InfoRetriever();

        Map<String, String> data = new HashMap<String, String>();
        data.put("action", this.action);
        data.put("project", project.getName());
        data.put("version", project.getVersion());
        data.put("groupId", project.getGroupId());
        data.put("artifactId", project.getArtifactId());
        data.put("hostName", info.getHostname());
        data.put("userName", info.getUsername());
        data.put("jvm", info.getJvm());
        data.put("encoding", info.getFileEncoding());

        // Envia dados
        try {
            DataSender ds = createDataSender();
            ds.sendData(data);
        } catch (CouldNotSendDataException e) {
            LOG.debug(e.getCause());
        }
    }

    /**
     * Cria data sender.
     * @return instancia de data sender.
     * @throws CouldNotSendDataException caso nao consiga enviar dados.
     */
    @SuppressWarnings("unchecked")
    private DataSender createDataSender() throws CouldNotSendDataException {
        try {
            Class<DataSender> dsClass = (Class<DataSender>) Class.forName(this.dataSender);
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
