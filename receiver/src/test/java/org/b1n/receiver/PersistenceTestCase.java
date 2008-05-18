package org.b1n.receiver;

import junit.framework.TestCase;

import org.b1n.framework.persistence.JpaUtil;

/**
 * @author Marcio Ribeiro
 * @date Jan 20, 2008
 */
public abstract class PersistenceTestCase extends TestCase {
    /**
     * Construtor.
     */
    public PersistenceTestCase() {
        // do nothing
    }

    /**
     * Construtor.
     * @param arg nome do teste.
     */
    public PersistenceTestCase(final String arg) {
        super(arg);
    }

    /**
     * Inicia sessao.
     * @throws Exception caso algo de errado ocorra.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        JpaUtil.getSession();
    }

    /**
     * Fecha sessao, faz rollback.
     * @throws Exception caso algo de errado ocorra.
     */
    @Override
    protected void tearDown() throws Exception {
        try {
            JpaUtil.getSession().getTransaction().setRollbackOnly();
            JpaUtil.closeSession();
        } finally {
            super.tearDown();
        }
    }
}
