package org.b1n.informer.core.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.b1n.framework.persistence.JpaEntity;

/**
 * Build.
 * @author Marcio Ribeiro
 * @date Jan 20, 2008
 */
@MappedSuperclass
public abstract class Build extends JpaEntity {
    @Column(nullable = false)
    private Date startTime;

    private Date endTime;

    private Boolean withTests;

    private Boolean deploy;

    /**
     * @return <code>true</code> se for deploy, <code>false</code> se nao.
     */
    public Boolean getDeploy() {
        return deploy;
    }

    /**
     * Define se build tem deploy envolvido.
     * @param deploy <code>true</code> se sim, <code>false</code> se nao.
     */
    public void setDeploy(final Boolean deploy) {
        this.deploy = deploy;
    }

    /**
     * @return <code>true</code> se build pulou os testes, <code>false</code> se nao.
     */
    public Boolean getWithTests() {
        return withTests;
    }

    /**
     * Define se pulou testes.
     * @param skipTests <code>true</code> se pulou testes, <code>false</code> caso contrario.
     */
    public void setWithTests(final Boolean skipTests) {
        this.withTests = skipTests;
    }

    /**
     * @return hora de inicio de build.
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Define hora de inicio de build.
     * @param startTime inicio de build.
     */
    public void setStartTime(final Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return fim de build.
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Define hora de fim de build.
     * @param endTime hora de fim de build.
     */
    public void setEndTime(final Date endTime) {
        this.endTime = endTime;
    }

    /**
     * @return delta entre fim e inicio de build em milisegundos.
     */
    public long getBuildTime() {
        return endTime.getTime() - startTime.getTime();
    }
}
