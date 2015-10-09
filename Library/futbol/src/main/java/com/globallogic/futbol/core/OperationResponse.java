package com.globallogic.futbol.core;

import java.io.Serializable;

/**
 * Created by Agustin Larghi on 07/10/2015.
 * Globallogic
 * agustin.larghi@globallogic.com
 */
public class OperationResponse<T> implements Serializable {
    private T result;
    private int resultCode;

    public OperationResponse(int aCode, T aResponse) {
        resultCode = aCode;
        result = aResponse;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
