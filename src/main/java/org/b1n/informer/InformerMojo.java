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
     * <code>true</code> se usuario tiver usado opcao -o (offline mode), false se nao.
     * @parameter default-value="${settings.offline}"
     * @required
     * @readonly
     */
    private final boolean offline = false;

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
     * Tempo minimo para considerar build.
     * @parameter default-value="20000"
     * @required
     */
    private Integer minimumBuildTime;

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
        if (offline) {
            return;
        }

        //        session.getSettings().

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
        if (masterProjectInfo.getBuildTime() < minimumBuildTime) {
            getLog().info("Fast build!");
            return;
        }

        try {
            final DataSender ds = getDataSender();

            final JSONObject json = new JSONObject();
            json.put("masterProject", masterProjectInfo);
            if (!MODULES.isEmpty()) {
                json.put("modules", MODULES.values());
            }
            ds.sendData(JSONSerializer.toJSON(json).toString());
        } catch (final CouldNotSendDataException e) {
            getLog().info(e.getCause());
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
            final Class<DataSender> dsClass = (Class<DataSender>) Class.forName(this.dataSenderClassName);
            final Constructor<DataSender> constructor = dsClass.getDeclaredConstructor(new Class[] { String.class });
            return constructor.newInstance(this.server);
        } catch (final NoSuchMethodException e) {
            throw new CouldNotSendDataException(e);
        } catch (final InstantiationException e) {
            throw new CouldNotSendDataException(e);
        } catch (final IllegalAccessException e) {
            throw new CouldNotSendDataException(e);
        } catch (final InvocationTargetException e) {
            throw new CouldNotSendDataException(e);
        } catch (final ClassNotFoundException e) {
            throw new CouldNotSendDataException(e);
        }
    }
}
