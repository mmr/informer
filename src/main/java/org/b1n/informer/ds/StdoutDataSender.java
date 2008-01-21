package org.b1n.informer.ds;

import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Data Sender que mostra parametros em stdout.
 * @author Marcio Ribeiro
 * @date Jan 19, 2008
 */
public class StdoutDataSender implements DataSender {
    /** Logger. */
    private static final Logger LOG = Logger.getLogger(DataSender.class);

    /**
     * Construtor.
     * @param serverUrl url de servidor.
     */
    public StdoutDataSender(String serverUrl) {
        // do nothing
    }

    /**
     * Envia dados.
     * @param data dados.
     * @throws CouldNotSendDataException caso nao consiga enviar dados.
     */
    public void sendData(Map<String, String> data) throws CouldNotSendDataException {
        for (Map.Entry<String, String> e : data.entrySet()) {
            LOG.debug(e.getKey() + " = " + e.getValue());
        }
    }
}
