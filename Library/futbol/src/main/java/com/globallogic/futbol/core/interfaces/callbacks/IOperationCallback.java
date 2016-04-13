package com.globallogic.futbol.core.interfaces.callbacks;

/**
 * A basic interface for the operation events callbacks
 *
 * @author facundo.mengoni
 * @since 0.3.0
 */
public interface IOperationCallback {
    /**
     * It is triggered once only when the operation starts.
     */
    void onStartOperation();

    /**
     * It is triggered once only when the operation was finished (whether successful or not).
     */
    void onFinishOperation();
}
