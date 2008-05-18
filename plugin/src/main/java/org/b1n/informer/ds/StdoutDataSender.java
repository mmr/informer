package org.b1n.informer.ds;

import org.apache.log4j.Logger;

/**
 * Data Sender que mostra parametros em stdout.
 * @author Marcio Ribeiro
 * @date Jan 19, 2008
 */
public class StdoutDataSender implements DataSender {
    private static final Logger LOG = Logger.getLogger(StdoutDataSender.class);

    /**
     * Construtor.
     * @param serverUrl url de servidor.
     */
    public StdoutDataSender(final String serverUrl) {
        // do nothing
    }

    /**
     * Mostra dados em stdout.
     * @param data dados.
     * @param maxAttempts numero maximo de tentativas.
     * @return resposta.
     * @throws CouldNotSendDataException caso nao consiga mostrar dados.
     */
    public String sendData(final String data, final Integer maxAttempts) throws CouldNotSendDataException {
        LOG.info(data);
        return null;
    }
}
