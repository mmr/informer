package org.b1n.informer.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.b1n.framework.persistence.SimpleEntity;

/**
 * Usuario.
 * @author Marcio Ribeiro
 * @date Jan 23, 2008
 */
@Entity
@Table(name = "builduser")
@SequenceGenerator(name = "seq_builduser", sequenceName = "seq_builduser")
public class User extends SimpleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_builduser")
    private Long id;

    @Column(nullable = false, unique = true)
    private String userName;

    /**
     * Construtor default para o Hibernate.
     */
    public User() {
        // nothing
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
     * Construtor.
     * @param userName nome do usuario.
     */
    public User(final String userName) {
        this.userName = userName;
    }

    /**
     * @return nome do usuario.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Define nome do usuario.
     * @param userName o nome do usuario que esta fazendo o build.
     */
    public void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * @return toString.
     */
    @Override
    public String toString() {
        return userName;
    }
}
