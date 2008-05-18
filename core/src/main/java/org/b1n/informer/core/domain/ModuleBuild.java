package org.b1n.informer.core.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

/**
 * Dados enviados pelo Informer.
 * @author Marcio Ribeiro
 * @date Jan 20, 2008
 */
@Entity
@SequenceGenerator(name = "seq_modulebuild", sequenceName = "seq_modulebuild")
public class ModuleBuild extends Build {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_modulebuild")
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ProjectBuild projectBuild;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Project project;

    /**
     * @return id.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Define id.
     * @param id o id.
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return o projeto.
     */
    public Project getProject() {
        return project;
    }

    /**
     * Define o projeto.
     * @param project o projeto.
     */
    public void setProject(final Project project) {
        this.project = project;
    }

    /**
     * @return o build do pai dese modulo.
     */
    public ProjectBuild getProjectBuild() {
        return projectBuild;
    }

    /**
     * Define o build do pai.
     * @param projectBuild build do pai.
     */
    public void setProjectBuild(final ProjectBuild projectBuild) {
        this.projectBuild = projectBuild;
    }
}
