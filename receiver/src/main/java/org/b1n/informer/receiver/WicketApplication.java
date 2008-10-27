package org.b1n.informer.receiver;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * @author Marcio Ribeiro (mmr)
 * @created Oct 26, 2008
 */
public class WicketApplication extends WebApplication {
    /**
     * Constructor.
     */
    public WicketApplication() {
        // nothing
    }

    /**
     * @return home page.
     */
    @Override
    public Class<? extends WebPage> getHomePage() {
        return LastBuilds.class;
    }
}
