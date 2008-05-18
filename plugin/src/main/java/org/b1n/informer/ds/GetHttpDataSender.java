package org.b1n.informer.ds;

import java.util.Map;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * Data Sender que usa HTTP.
 * @author Marcio Ribeiro
 * @date Jan 19, 2008
 */
public class GetHttpDataSender extends HttpDataSender {
    private static final int MAX_GET_BYTES = 1024;

    /**
     * Construtor.
     * @param serverUrl url de servidor.
     */
    public GetHttpDataSender(final String serverUrl) {
        super(serverUrl);
    }

    /**
     * Devolve metodo get populado com parametros para request.
     * @param data dados.
     * @return metodo.
     */
    @Override
    protected HttpMethod getMethod(final Map<String, String> data) {
        final GetMethod method = new GetMethod(getServerUrl());
        method.setQueryString(getNameValuePairs(data));
        return method;
    }

    /**
     * Verifica se request nao tem mais bytes que o permitido em metodo GET.
     * @param method metodo http.
     * @throws CouldNotSendDataException caso o request tenha mais dados que o permitido.
     */
    @Override
    protected void checkRequest(final HttpMethod method) throws CouldNotSendDataException {
        if (method.getQueryString().length() >= MAX_GET_BYTES) {
            throw new CouldNotSendDataException("Requisicao invalida: maior que " + MAX_GET_BYTES + " bytes.");
        }
    }
}
