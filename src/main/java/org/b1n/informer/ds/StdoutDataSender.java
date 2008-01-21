package org.b1n.informer.ds;

import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Data Sender que mostra parametros em stdout.
 * @author Marcio Ribeiro
 * @date Jan 19, 2008
 */
public class StdoutDataSender implements DataSender {
    private static long cx = 0;

    /** Logger. */
    private static final Logger LOG = Logger.getLogger(DataSender.class);

    /**
     * Construtor.
     * @param serverUrl url de servidor.
     */
    public StdoutDataSender(String serverUrl) {
        // do nothing
    }

    public void sendEndBuild(Map<String, String> data) throws CouldNotSendDataException {
        showData(data);
    }

    public void sendEndModule(Map<String, String> data) throws CouldNotSendDataException {
        showData(data);
    }

    public long sendStartBuild(Map<String, String> data) throws CouldNotSendDataException {
        return showData(data);
    }

    public long sendStartModule(Map<String, String> data) throws CouldNotSendDataException {
        return showData(data);
    }

    private long showData(Map<String, String> data) throws CouldNotSendDataException {
        for (Map.Entry<String, String> e : data.entrySet()) {
            LOG.info(e.getKey() + " = " + e.getValue());
        }
        return cx++;
    }
}
