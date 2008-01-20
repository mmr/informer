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
    /**
     * Construtor.
     * @param serverUrl url de servidor.
     */
    public GetHttpDataSender(String serverUrl) {
        super(serverUrl);
    }

    /**
     * Devolve metodo get populado com parametros para request.
     */
    @Override
    protected HttpMethod getMethod(Map<String, String> data) {
        GetMethod method = new GetMethod(getServerUrl());
        method.setQueryString(getNameValuePairs(data));
        return method;
    }
}
