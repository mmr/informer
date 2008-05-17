package org.b1n.informer.ds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.Logger;

/**
 * Data Sender que usa HTTP.
 * @author Marcio Ribeiro
 * @date Jan 19, 2008
 */
public abstract class HttpDataSender implements DataSender {
    /** Sever url. */
    private final String serverUrl;

    /** Numero maximo de tentativas para tolerancia a falha de rede. */
    private static final int MAX_ATTEMPTS = 3;

    /** Logger. */
    private static final Logger LOG = Logger.getLogger(HttpDataSender.class);

    /**
     * Construtor.
     * @param serverUrl url de servidor.
     */
    public HttpDataSender(final String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * Envia dados para servidor.
     * @param data dados.
     * @throws CouldNotSendDataException caso nao consiga enviar dados.
     * @return resposta.
     */
    public String sendData(final String data) throws CouldNotSendDataException {
        final Map<String, String> d = new HashMap<String, String>();
        d.put("buildInfo", data);

        int attempt = 0;
        final HttpMethod method = getMethod(d);
        try {
            while (attempt < MAX_ATTEMPTS) {
                try {
                    final int statusCode = new HttpClient().executeMethod(method);
                    if (statusCode == HttpStatus.SC_OK) {
                        LOG.debug(method.getURI() + " : OK");
                        return method.getResponseBodyAsString();
                    }
                } catch (final IOException e) {
                    // do nothing, try again
                    LOG.debug(e);
                }
                attempt++;
            }
        } finally {
            method.releaseConnection();
        }

        throw new CouldNotSendDataException("Error: " + method.getStatusLine());
    }

    /**
     * Devolve metodo a ser usado.
     * @param data dados.
     * @return metodo.
     */
    protected abstract HttpMethod getMethod(Map<String, String> data);

    /**
     * Metodo auxiliar que cria array de NameValuePair a partir de mapa.
     * @param data mapa de dados.
     * @return array de NameValuePair.
     */
    protected NameValuePair[] getNameValuePairs(final Map<String, String> data) {
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (final Map.Entry<String, String> entry : data.entrySet()) {
            params.add(new NameValuePair(entry.getKey(), entry.getValue()));
        }
        return params.toArray(new NameValuePair[params.size()]);
    }

    /**
     * @return url de servidor.
     */
    protected final String getServerUrl() {
        return this.serverUrl;
    }
}
