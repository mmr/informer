package org.b1n.informer.receiver.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author Marcio Ribeiro
 * @date Feb 4, 2008
 */
public abstract class WriteTagSupport extends BodyTagSupport {
    /**
     * Escreve para pagina.
     * @param str string a ser escrita.
     * @throws JspException caso algo de inesperado ocorra ao escrever para pagina.
     */
    protected void write(final String str) throws JspException {
        try {
            pageContext.getOut().write(str);
        } catch (final IOException e) {
            throw new JspException(e);
        }
    }
}
