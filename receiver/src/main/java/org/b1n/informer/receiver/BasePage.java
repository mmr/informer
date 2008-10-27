package org.b1n.informer.receiver;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

/**
 * @author Marcio Ribeiro (mmr)
 * @created Oct 27, 2008
 */
public abstract class BasePage extends WebPage {
    /**
     * Construtor.
     */
    public BasePage() {
        add(new BookmarkablePageLink("LastBuilds", LastBuilds.class));
        add(new BookmarkablePageLink("BuildStats", BuildStats.class));
        add(new Label("PageTitle", getPageTitle()));
    }

    /**
     * @return titulo da pagina.
     */
    protected abstract String getPageTitle();
}
