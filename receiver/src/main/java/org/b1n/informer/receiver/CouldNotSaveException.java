package org.b1n.informer.receiver;

/**
 * Caso nao consiga salvar dados.
 * @author Marcio Ribeiro
 * @date Jan 21, 2008
 */
public class CouldNotSaveException extends Exception {
    /**
     * Construtor.
     * @param e causa.
     */
    public CouldNotSaveException(final Throwable e) {
        super(e);
    }

    /**
     * Construtor.
     * @param msg mensagem.
     */
    public CouldNotSaveException(final String msg) {
        super(msg);
    }
}
