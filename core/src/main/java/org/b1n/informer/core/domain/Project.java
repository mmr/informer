package org.b1n.informer.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.b1n.framework.persistence.SimpleEntity;

/**
 * Projeto.
 * @author Marcio Ribeiro
 * @date Jan 21, 2008
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "groupId", "artifactId", "version" }) })
@SequenceGenerator(name = "seq_project", sequenceName = "seq_project")
public class Project extends SimpleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_project")
    private Long id;

    @Column(nullable = false)
    private String groupId;

    @Column(nullable = false)
    private String artifactId;

    @Column(nullable = false)
    private String version;

    private String projectName;

    /**
     * Construtor default para o hibernate.
     */
    public Project() {
        // nothing
    }

    /**
     * Construtor.
     * @param groupId id de grupo do artefato.
     * @param artifactId id do artefato.
     * @param version versao do artefato.
     * @param projectName nome do projeto.
     */
    public Project(final String groupId, final String artifactId, final String version, final String projectName) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.projectName = projectName;
    }

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
     * @return nome do projeto.
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Define o nome do projeto.
     * @param projectName nome do projeto.
     */
    public void setProjectName(final String projectName) {
        this.projectName = projectName;
    }

    /**
     * @return id de artefato do grupo.
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * Define o id do grupo.
     * @param groupId id do grupo.
     */
    public void setGroupId(final String groupId) {
        this.groupId = groupId;
    }

    /**
     * @return o id do artefato.
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * Define o id de termo.
     * @param artifactId o artefato
     */
    public void setArtifactId(final String artifactId) {
        this.artifactId = artifactId;
    }

    /**
     * @return versao.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Define versao.
     * @param version versao.
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * @return toString.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(groupId).append(" / ").append(artifactId).append(" ").append(version);
        return sb.toString();
    }
}
