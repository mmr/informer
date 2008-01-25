package org.b1n.informer.ds;

/**
 * Data Sender que mostra parametros em stdout.
 * @author Marcio Ribeiro
 * @date Jan 19, 2008
 */
public class StdoutDataSender implements DataSender {
    private static long cx = 0;

    /**
     * Construtor.
     * @param serverUrl url de servidor.
     */
    public StdoutDataSender(String serverUrl) {
        // do nothing
    }

    public String sendData(String data) throws CouldNotSendDataException {
        System.out.println(data);
        return String.valueOf(cx++);
    }
}
