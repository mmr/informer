package org.b1n.informer.ds;

/**
 * Criada se nao conseguir criar data sender.
 * @author Marcio Ribeiro
 * @date May 18, 2008
 */
public class CouldNotCreateDataSenderException extends RuntimeException {
    /**
     * @param e causa.
     */
    public CouldNotCreateDataSenderException(final Throwable e) {
        super(e);
    }

    /**
     * @param msg mensagem.
     */
    public CouldNotCreateDataSenderException(final String msg) {
        super(msg);
    }

    /**
     * @param msg mensagem.
     * @param e causa.
     */
    public CouldNotCreateDataSenderException(final String msg, final Throwable e) {
        super(msg, e);
    }
}
