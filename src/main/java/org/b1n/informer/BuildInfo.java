package org.b1n.informer;

import java.util.Date;

import org.apache.maven.project.MavenProject;

/**
 * @author Marcio Ribeiro
 * @date Jan 24, 2008
 */
public abstract class BuildInfo {
    private MavenProject project;

    private Date startTime;

    private Date endTime;

    /**
     * Construtor.
     * @param project projeto.
     * @param startTime hora de inicio.
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
     * @return hora do fim do build.
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Define a hora de termino do build.
     * @param endTime hora do fim do build.
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
}
