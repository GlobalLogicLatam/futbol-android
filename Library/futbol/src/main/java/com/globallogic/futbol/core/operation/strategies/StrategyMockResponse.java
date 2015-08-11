package com.globallogic.futbol.core.operation.strategies;

import java.io.Serializable;

/**
 * A wrapper that contains an http response code and a string returned by the server.
 *
 * @author Facundo Mengoni | GlobalLogic
 * @author facundo.mengoni@globallogic.com
 */
public class StrategyMockResponse implements Serializable {
    private Integer httpCode;
    private String response;

    public StrategyMockResponse() {
    }

    public StrategyMockResponse(int httpCode, String response) {
        this.httpCode = httpCode;
        this.response = response;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}