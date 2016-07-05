package com.globallogic.futbol.core.responses;

/**
 * A wrapper that contains an object as response of get something from de Shared preference.
 * It represent a specific responses from the Shared preference.
 *
 * @author facundo.mengoni
 * @since 0.3.6
 */
public class StrategySharedPreferenceResponse<T> extends StrategyResponse {
    private T result;

    public StrategySharedPreferenceResponse() {
    }

    public StrategySharedPreferenceResponse(T aResult) {
        this.result = aResult;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T aResult) {
        this.result = aResult;
    }

    @Override
    public String toString() {
        return "StrategySharedPreferenceResponse{" +
                "result=" + result +
                "} ";
    }
}