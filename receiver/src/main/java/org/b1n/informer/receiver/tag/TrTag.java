package org.b1n.informer.receiver.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.core.LoopTagStatus;

import org.b1n.informer.core.domain.Build;

/**
 * @author Marcio Ribeiro
 * @date Feb 4, 2008
 */
public class TrTag extends WriteTagSupport {
    private Object build;
    private Object status;

    /**
     * Cria linha de build, com estilos diferentes para par/impar e diferenciacoes quando build for sem testes e/ou deploy.
     * @return EVAL_PAGE.
     * @throws JspException caso algo de inesperado ocorra.
     */
    @Override
    public int doStartTag() throws JspException {
        final Build b = (Build) build;
        final LoopTagStatus s = (LoopTagStatus) status;

        final StringBuilder sb = new StringBuilder();
        sb.append("<tr class=\"");

        // Even / Odd
        if (s.getIndex() % 2 == 0) {
            sb.append("even");
        } else {
            sb.append("odd");
        }

        // Deploy
        if (b.getDeploy()) {
            sb.append(" deploy");
        }

        // Tests
        if (!b.getWithTests()) {
            sb.append(" withoutTests");
        }

        sb.append("\">");

        write(sb.toString());
        return EVAL_BODY_INCLUDE;
    }

    /**
     * Termina tag.
     * @return EVAL_PAGE.
     * @throws JspException caso algo de inesperado ocorra ao terminar tag.
     */
    @Override
    public int doEndTag() throws JspException {
        write("</tr>");
        return EVAL_PAGE;
    }

    /**
     * Define build.
     * @param build build.
     */
    public void setBuild(final Object build) {
        this.build = build;
    }

    /**
     * Define status.
     * @param status status.
     */
    public void setStatus(final Object status) {
        this.status = status;
    }
}
