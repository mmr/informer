package org.b1n.informer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
import org.b1n.informer.ds.CouldNotCreateDataSenderException;
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
    private static final Map<String, BuildInfo> MODULES = new HashMap<String, BuildInfo>();

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
     * Tempo minimo para considerar build (em milissegundos).
     * @parameter default-value="20000"
     * @required
     */
    private Integer minBuildTime;

    /**
     * Numero maximo de tentativas para enviar report.
     * @parameter default-value="5"
     * @required
     */
    private Integer maxAttempts;

    /**
     * Classe do data sender.
     * @parameter default-value="org.b1n.informer.ds.PostHttpDataSender"
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
        if (session.getSettings().isOffline()) {
            return;
        }

        // Inicia build
        if (project.isExecutionRoot()) {
            if (action.equals(START_ACTION)) {
                lastProject = reactorProjects.get(reactorProjects.size() - 1);
                startTimeMasterProject();
            } else if (action.equals(END_ACTION) && lastProject.equals(project)) {
                // Projeto sem filhos
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
            sendBuildInfo();
        }
    }

    /**
     * Marca inicio de build de projeto pai.
     */
    private void startTimeMasterProject() {
        masterProjectInfo = new MasterProjectInfo(project, session);
    }

    /**
     * Marca fim de build de projeto pai.
     */
    private void endTimeMasterProject() {
        masterProjectInfo.calculateBuildTime();
    }

    /**
     * Marca inicio de build de modulo.
     */
    private void startTimeModule() {
        MODULES.put(project.getId(), new BuildInfo(project, session));
    }

    /**
     * Marca fim de build de modulo.
     */
    private void endTimeModule() {
        MODULES.get(project.getId()).calculateBuildTime();
    }

    /**
     * Envia mensagem de fim de build.
     */
    private void sendBuildInfo() {
        endTimeMasterProject();
        if (masterProjectInfo.getBuildTime() < minBuildTime) {
            getLog().info("Fast build!");
            return;
        }

        final DataSender ds = getDataSender();
        final JSONObject json = new JSONObject();
        json.put("masterProject", masterProjectInfo);
        if (!MODULES.isEmpty()) {
            json.put("modules", MODULES.values());
        }
        String data = JSONSerializer.toJSON(json).toString();

        try {
            ds.sendData(data, maxAttempts);
        } catch (final CouldNotSendDataException e) {
            getLog().error("Nao foi possivel enviar dados!");
            getLog().debug("Server: " + server);
            getLog().debug("Causa: " + e);
        }
        getLog().debug("Dados: " + data);

    }

    /**
     * Cria data sender.
     * @return instancia de data sender.
     */
    @SuppressWarnings("unchecked")
    private DataSender getDataSender() {
        if (dataSender != null) {
            return dataSender;
        }
        try {
            final Class<DataSender> dsClass = (Class<DataSender>) Class.forName(this.dataSenderClassName);
            final Constructor<DataSender> constructor = dsClass.getDeclaredConstructor(new Class[] { String.class });
            return constructor.newInstance(this.server);
        } catch (final NoSuchMethodException e) {
            throw new CouldNotCreateDataSenderException(e);
        } catch (final InstantiationException e) {
            throw new CouldNotCreateDataSenderException(e);
        } catch (final IllegalAccessException e) {
            throw new CouldNotCreateDataSenderException(e);
        } catch (final InvocationTargetException e) {
            throw new CouldNotCreateDataSenderException(e);
        } catch (final ClassNotFoundException e) {
            throw new CouldNotCreateDataSenderException(e);
        }
    }
}
