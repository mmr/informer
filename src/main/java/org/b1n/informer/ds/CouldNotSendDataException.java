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
    public CouldNotSendDataException(Throwable e) {
        super(e);
    }

    /**
     * @param msg mensagem.
     */
    public CouldNotSendDataException(String msg) {
        super(msg);
    }

    /**
     * @param msg mensagem.
     * @param e causa.
     */
    public CouldNotSendDataException(String msg, Throwable e) {
        super(msg, e);
    }
}
