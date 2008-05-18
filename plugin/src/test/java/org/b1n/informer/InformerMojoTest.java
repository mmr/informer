package org.b1n.informer;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

/**
 * @author Marcio Ribeiro
 * @date Jan 25, 2008
 */
public class InformerMojoTest extends AbstractMojoTestCase {
    /**
     * Setup.
     * @throws Exception caso algo de errado ocorra ao fazer setup.
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Teste de plugin.
     * @throws Exception caso algo de errado aconteca.
     */
    public void testInformer() throws Exception {
        final File testPom = new File(getBasedir(), "target/test-classes/plugin-config.xml");
        final InformerMojo mojo = (InformerMojo) lookupMojo("informer", testPom);
        assertNotNull(mojo);
    }
}
