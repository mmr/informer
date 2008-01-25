package org.b1n.informer.ds;

/**
 * Envia dados para servidor.
 * @author Marcio Ribeiro
 * @date Jan 19, 2008
 */
public interface DataSender {
    /**
     * Envia dados sobre build.
     * @param data dados.
     * @return identificador de build.
     * @throws CouldNotSendDataException caso nao consiga enviar dados.
     */
    String sendData(String data) throws CouldNotSendDataException;
}
