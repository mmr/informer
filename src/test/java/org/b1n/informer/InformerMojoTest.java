package org.b1n.informer;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

/**
 * @author Marcio Ribeiro
 * @date Jan 25, 2008
 */
public class InformerMojoTest extends AbstractMojoTestCase {
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * tests the proper discovery and configuration of the mojo
     * @throws Exception
     */
    public void testInformer() throws Exception {
        File testPom = new File(getBasedir(), "target/test-classes/plugin-config.xml");
        InformerMojo mojo = (InformerMojo) lookupMojo("informer", testPom);
        assertNotNull(mojo);
    }
}
