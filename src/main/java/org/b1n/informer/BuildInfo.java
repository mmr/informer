package org.b1n.informer;

import java.util.Date;

import org.apache.maven.project.MavenProject;

/**
 * @author Marcio Ribeiro
 * @date Jan 24, 2008
 */
public class BuildInfo {
    private MavenProject project;

    private Date startTime;

    private long buildTime;

    /**
     * Construtor.
     * @param project projeto.
     */
    public BuildInfo(MavenProject project) {
        this(project, new Date());
    }

    /**
     * Construtor.
     * @param project projeto.
     * @param startTime hora de inicio.
     */
    public BuildInfo(MavenProject project, Date startTime) {
        this.project = project;
        this.startTime = startTime;
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
     * @return inicio de build.
     */
    public Date getStartTime() {
        return startTime;
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
        long currentTs = new Date().getTime();
        buildTime = currentTs - startTime.getTime();
    }
}
