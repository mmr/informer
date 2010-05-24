package org.b1n.informer;

import java.util.Date;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

/**
 * @author Marcio Ribeiro
 * @date Jan 24, 2008
 */
public class BuildInfo {
    private MavenProject project;

    private MavenSession session;

    private Date startTime;

    private long buildTime;

    /**
     * Construtor.
     * @param project projeto.
     * @param session sessao do maven.
     */
    public BuildInfo(final MavenProject project, final MavenSession session) {
        this(project, session, new Date());
    }

    /**
     * Construtor.
     * @param project projeto.
     * @param session sessao do maven.
     * @param startTime hora de inicio.
     */
    public BuildInfo(final MavenProject project, final MavenSession session, final Date startTime) {
        this.project = project;
        this.startTime = startTime;
        this.session = session;
    }

    /**
     * @return <code>true</code> se build esta executando testes, <code>false</code> caso contrario.
     */
    public boolean isWithTests() {
        return !"true".equals(System.getProperty("maven.test.skip")) && System.getProperty("skipTests") == null;
    }

    /**
     * @return <code>true</code> se alem de install tiver deploy envolvido, <code>false</code> caso contrario.
     */
    public boolean isDeploy() {
        return session.getGoals().contains("deploy");
    }

    /**
     * @return versao do artefato.
     */
    public String getVersion() {
        return project.getVersion();
    }

    /**
     * @return id do grupo do artefato.
     */
    public String getGroupId() {
        return project.getGroupId();
    }

    /**
     * @return id do artefato.
     */
    public String getArtifactId() {
        return project.getArtifactId();
    }

    /**
     * @return nome do projeto.
     */
    public String getProjectName() {
        return project.getName();
    }

    /**
     * @return o tempo de build em milisegundos.
     */
    public long getBuildTime() {
        return buildTime;
    }

    /**
     * Calcula o tempo de build e define o buildTime.
     */
    public void calculateBuildTime() {
        final long currentTs = new Date().getTime();
        buildTime = currentTs - startTime.getTime();
    }
}
