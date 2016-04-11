package com.globallogic.futbol.core.responses;

/**
 * A wrapper that contains an http code and a string returned by the server.
 * It represent a specific responses from the server.
 *
 * @author facundo.mengoni
 * @since 0.3.0
 */
public class StrategyHttpResponse extends StrategyResponse {
    private Integer httpCode;
    private String response;

    public StrategyHttpResponse() {
    }

    public StrategyHttpResponse(int httpCode, String response) {
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

    @Override
    public String toString() {
        return "StrategyMockResponse{" +
                "httpCode=" + httpCode +
                ", response='" + response + '\'' +
                '}';
    }
}