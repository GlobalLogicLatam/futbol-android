package com.globallogic.futbol.core.interfaces.callbacks;

/**
 * A basic interface for the http strategy events
 *
 * @author facun.do.mengoni
 * @since 0.3.0
 */
public interface IStrategyHttpCallback extends IOperationCallback {
    /**
     * It is triggered when the strategy can't be executed because don't have connection.
     */
    void onNoInternet();
}