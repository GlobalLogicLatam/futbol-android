package com.globallogic.futbol.core.responses;

/**
 * A wrapper that contains an object as response of get something from de DB.
 * It represent a specific responses from the DB.
 *
 * @author facundo.mengoni
 * @since 0.3.0
 */
public class StrategyDbResponse<T> extends StrategyResponse {
    private T response;

    public StrategyDbResponse() {
    }

    public StrategyDbResponse(T response) {
        this.response = response;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "StrategyDbResponse{" +
                "response=" + response +
                '}';
    }
}