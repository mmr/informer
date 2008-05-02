package org.b1n.informer.ds;

/**
 * Lancada quando nao conseguir enviar dados para servidor.
 * @author Marcio Ribeiro
 * @date Jan 19, 2008
 */
public class CouldNotSendDataException extends Exception {
    /**
     * @param e causa.
     */
    public CouldNotSendDataException(final Throwable e) {
        super(e);
    }

    /**
     * @param msg mensagem.
     */
    public CouldNotSendDataException(final String msg) {
        super(msg);
    }

    /**
     * @param msg mensagem.
     * @param e causa.
     */
    public CouldNotSendDataException(final String msg, final Throwable e) {
        super(msg, e);
    }
}
