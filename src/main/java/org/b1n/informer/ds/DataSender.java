package org.b1n.informer.ds;

import java.util.Map;

/**
 * Envia dados para servidor.
 * @author Marcio Ribeiro
 * @date Jan 19, 2008
 */
public interface DataSender {
    /**
     * Envia dados para servidor.
     * @param data dados.
     * @throws CouldNotSendDataException caso nao consiga enviar dados.
     */
    void sendData(Map<String, String> data) throws CouldNotSendDataException;
}
