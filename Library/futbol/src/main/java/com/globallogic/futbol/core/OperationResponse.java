package com.globallogic.futbol.core;

import java.io.Serializable;

/**
 * Created by Agustin Larghi on 07/10/2015.
 * Globallogic
 * agustin.larghi@globallogic.com
 */
public class OperationResponse<Z, T> implements Serializable {
    private T result;
    private Z resultCode;

    public OperationResponse(Z aCode, T aResponse) {
        resultCode = aCode;
        result = aResponse;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Z getResultCode() {
        return resultCode;
    }

    public void setResultCode(Z resultCode) {
        this.resultCode = resultCode;
    }
}
