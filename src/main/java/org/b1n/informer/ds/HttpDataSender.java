package org.b1n.informer.ds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;

/**
 * Data Sender que usa HTTP.
 * @author Marcio Ribeiro
 * @date Jan 19, 2008
 */
public abstract class HttpDataSender implements DataSender {
    private String serverUrl;

    /**
     * Construtor.
     * @param serverUrl url de servidor.
     */
    public HttpDataSender(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * Envia dados.
     * @param data dados.
     */
    public void sendData(Map<String, String> data) throws CouldNotSendDataException {
        HttpMethod method = getMethod(data);
        try {
            int statusCode = new HttpClient().executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                throw new CouldNotSendDataException("Error: " + method.getStatusLine());
            }
        } catch (IOException e) {
            throw new CouldNotSendDataException(e);
        } finally {
            method.releaseConnection();
        }
    }

    protected abstract HttpMethod getMethod(Map<String, String> data);

    /**
     * Metodo auxiliar que cria array de NameValuePair a partir de mapa.
     * @param data mapa de dados.
     * @return array de NameValuePair.
     */
    protected NameValuePair[] getNameValuePairs(Map<String, String> data) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            params.add(new NameValuePair(entry.getKey(), entry.getValue()));
        }
        return (NameValuePair[]) params.toArray();
    }

    /**
     * @return url de servidor.
     */
    protected final String getServerUrl() {
        return this.serverUrl;
    }
}
