package org.b1n.informer.receiver.tag;

/**
 * @author Marcio Ribeiro
 * @date Feb 4, 2008
 */
public abstract class ValueTagSupport extends WriteTagSupport {
    private Object value;

    /**
     * @return valor.
     */
    protected Object getValue() {
        return this.value;
    }

    /**
     * Define valor.
     * @param value valor.
     */
    public void setValue(final Object value) {
        this.value = value;
    }
}
