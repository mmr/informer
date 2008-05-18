package org.b1n.informer.core;

import junit.framework.TestCase;

import org.b1n.framework.persistence.JpaUtil;
import org.hsqldb.Server;

/**
 * @author Marcio Ribeiro (mmr)
 * @created Mar 28, 2007
 */
public abstract class PersistenceTestCase extends TestCase {
    private static final Server SERVER;

    static {
        // Start HSQLDB Server programatically
        SERVER = new Server();
        SERVER.putPropertiesFromString("database.0=mem:test");
        SERVER.putPropertiesFromString("dbname.0=test");
        SERVER.start();
    }

    /**
     * Constructor.
     */
    public PersistenceTestCase() {
        // do nothing
    }

    /**
     * Setup.
     * @throws Exception exception.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        JpaUtil.getSession();
    }

    /**
     * Tear down.
     * @throws Exception exception.
     */
    @Override
    protected void tearDown() throws Exception {
        try {
            JpaUtil.closeSession();
        } finally {
            super.tearDown();
        }
    }
}