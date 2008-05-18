package org.b1n.informer.receiver.tag;

import java.text.NumberFormat;

import javax.servlet.jsp.JspException;

/**
 * @author Marcio Ribeiro
 * @date Feb 4, 2008
 */
public class BuildTimeTag extends ValueTagSupport {
    private static final NumberFormat NF;

    static {
        NF = NumberFormat.getInstance();
        NF.setMinimumIntegerDigits(2);
    }

    /**
     * Trata tag.
     * @return EVAL_PAGE.
     * @throws JspException caso algo de inesperado ocorra ao tratar tag.
     */
    @Override
    public int doEndTag() throws JspException {
        final int secsInMin = 60;
        final int secsInMili = 1000;

        final long buildTime = (Long) getValue();

        final int sec = (int) (buildTime / secsInMili);

        final StringBuilder sb = new StringBuilder();
        final int mins = sec / secsInMin;
        if (mins > 0) {
            sb.append(NF.format(mins)).append("\"");
        }

        sb.append(NF.format(sec % secsInMin)).append("'");
        write(sb.toString());
        return SKIP_BODY;
    }
}
