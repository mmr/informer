package org.b1n.informer.receiver;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.b1n.framework.persistence.JpaUtil;

/**
 * @author Marcio Ribeiro
 * @date Jan 20, 2008
 */
public class TransactionFilter implements Filter {

    /**
     * Aplica filtro.
     * @param req request.
     * @param resp response.
     * @param chain chain de filtros.
     * @throws IOException caso algo de inesperado ocorra.
     * @throws ServletException caso algo de inesperado ocorra.
     */
    public void doFilter(final ServletRequest req, final ServletResponse resp, final FilterChain chain) throws IOException, ServletException {
        JpaUtil.getSession();
        chain.doFilter(req, resp);
        JpaUtil.closeSession();
    }

    /**
     * Destroy.
     */
    public void destroy() {
        // do nothing
    }

    /**
     * Init.
     * @param arg0 config.
     * @throws ServletException caso algo de inesperado ocorra.
     */
    public void init(final FilterConfig arg0) throws ServletException {
        // do nothing
    }

}
