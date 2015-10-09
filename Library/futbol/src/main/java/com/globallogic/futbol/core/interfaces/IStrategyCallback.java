package com.globallogic.futbol.core.interfaces;

import com.globallogic.futbol.core.OperationResponse;

public interface IStrategyCallback<T> {
    /**
     * Analysis of the response returned by the server
     *
     * @param aException the exception thrown because of some error
     */
    void parseResponse(Exception aException, OperationResponse<T> response);
}