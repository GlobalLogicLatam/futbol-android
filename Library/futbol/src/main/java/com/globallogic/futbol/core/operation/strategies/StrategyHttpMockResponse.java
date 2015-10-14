package com.globallogic.futbol.core.operation.strategies;

/**
 * A wrapper that contains an http response code and a string returned by the server.
 *
 * @author Facundo Mengoni | GlobalLogic
 * @author facundo.mengoni@globallogic.com
 */
public class StrategyHttpMockResponse extends StrategyMockResponse<String> {

    public StrategyHttpMockResponse(int httpCode, String response) {
        setHttpCode(httpCode);
        setStream(response);
    }

    public int getHttpCode() {
        return getIntCode();
    }

    public void setHttpCode(int httpCode) {
        setIntCode(httpCode);
    }

    public String getResponse() {
        return getStream();
    }
}