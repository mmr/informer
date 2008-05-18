package org.b1n.informer.receiver.tag;

import javax.servlet.jsp.JspException;

/**
 * @author Marcio Ribeiro
 * @date Feb 4, 2008
 */
public class BoolTag extends ValueTagSupport {
    private static final String STRING_TRUE = "Sim";
    private static final String STRING_FALSE = "Não";

    /**
     * Trata tag.
     * @return EVAL_PAGE.
     * @throws JspException caso algo de inesperado ocorra ao tratar tag.
     */
    @Override
    public int doEndTag() throws JspException {
        write(getBoolString());
        return SKIP_BODY;
    }

    /**
     * @return string para boolean.
     */
    private String getBoolString() {
        if (Boolean.TRUE.equals(getValue())) {
            return STRING_TRUE;
        } else {
            return STRING_FALSE;
        }
    }
}
