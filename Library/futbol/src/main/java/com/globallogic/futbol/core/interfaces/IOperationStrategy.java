package com.globallogic.futbol.core.interfaces;

import java.io.Serializable;

public interface IOperationStrategy extends Serializable {
    /**
     * It is responsible for executing the operation to get the result.
     *
     * @param callback The callback for the response. You should be stored a reference to this because it can be updated later.
     */
    void doRequest(IStrategyCallback callback);

    /**
     * Update of callback reference
     */
    void updateCallback(IStrategyCallback callback);
}