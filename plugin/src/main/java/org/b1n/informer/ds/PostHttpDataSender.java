package org.b1n.informer.ds;

import java.util.Map;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * Data Sender que usa HTTP.
 * @author Marcio Ribeiro
 * @date Jan 19, 2008
 */
public class PostHttpDataSender extends HttpDataSender {
    /**
     * Construtor.
     * @param serverUrl url de servidor.
     */
    public PostHttpDataSender(final String serverUrl) {
        super(serverUrl);
    }

    /**
     * Devolve metodo get populado com parametros para request.
     * @param data dados.
     * @return o metodo.
     */
    @Override
    protected HttpMethod getMethod(final Map<String, String> data) {
        final PostMethod method = new PostMethod(getServerUrl());
        method.setRequestBody(getNameValuePairs(data));
        return method;
    }
}
