package org.b1n.informer.core.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

/**
 * Build.
 * @author Marcio Ribeiro
 * @date Jan 20, 2008
 */
@Entity
@SequenceGenerator(name = "seq_projectbuild", sequenceName = "seq_projectbuild")
public class ProjectBuild extends Build {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_projectbuild")
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Project project;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectBuild")
    private final List<ModuleBuild> modules = new ArrayList<ModuleBuild>();

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    private Host host;

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
     * @return o projeto que esta sendo construido.
     */
    public Project getProject() {
        return project;
    }

    /**
     * Define o projeto que esta sendo construido.
     * @param project o projeto.
     */
    public void setProject(final Project project) {
        this.project = project;
    }

    /**
     * @return os modulos do projeto que esta sendo construido.
     */
    public List<ModuleBuild> getModules() {
        return modules;
    }

    /**
     * Adiciona um modulo a lista de modulos dessa construcao de projeto.
     * @param module modulo.
     */
    public void addModule(final ModuleBuild module) {
        this.modules.add(module);
    }

    /**
     * @return usuario que esta fazendo build.
     */
    public User getUser() {
        return user;
    }

    /**
     * Define o usuario.
     * @param user o usuario.
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * @return o host onde o build esta sendo feito.
     */
    public Host getHost() {
        return host;
    }

    /**
     * Define o host onde o build esta sendo feito.
     * @param host o host.
     */
    public void setHost(final Host host) {
        this.host = host;
    }
}
