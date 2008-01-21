package org.b1n.informer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
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
 * @aggregator
 */
public class InformerMojo extends AbstractMojo {
    /** Info about calls. */
    private static final ThreadLocal<Map<MavenProject, String>> CALLS;
    static {
        CALLS = new ThreadLocal<Map<MavenProject, String>>();
        CALLS.set(new HashMap<MavenProject, String>());
    }

    /**
     * The maven project.
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * The maven session.
     * @parameter expression="${session}"
     * @required
     * @readonly
     */
    private MavenSession session;

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

    /**
     * Let the magic begin.
     * @throws MojoExecutionException erro.
     * @throws MojoFailureException falha.
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        //        calls.put(project.getArtifactId(), new Date());

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

        DataSender ds = createDataSender();
        if (ds != null) {
            try {
                ds.sendData(data);
            } catch (CouldNotSendDataException e) {
                LOG.error(e.getCause());
            }
        }
        //
        //        for (Map.Entry<String, Date> e : calls.entrySet()) {
        //            LOG.error(e.getKey() + " : " + e.getValue());
        //        }
    }

    /**
     * Cria data sender.
     * @return instancia de data sender.
     */
    @SuppressWarnings("unchecked")
    private DataSender createDataSender() {
        try {
            Class<DataSender> dsClass = (Class<DataSender>) Class.forName(this.dataSender);
            Constructor<DataSender> constructor = dsClass.getDeclaredConstructor(new Class[] { String.class });
            return constructor.newInstance(this.server);
        } catch (NoSuchMethodException e) {
            LOG.error(e);
        } catch (InstantiationException e) {
            LOG.error(e);
        } catch (IllegalAccessException e) {
            LOG.error(e);
        } catch (InvocationTargetException e) {
            LOG.error(e);
        } catch (ClassNotFoundException e) {
            LOG.error(e);
        }
        return null;
    }
}
