package com.globallogic.futbol.core.interfaces;

public interface IOperationReceiver {
    /**
     * Called when the operation is starting. This is where the view should inform await the result.
     *
     * @see com.globallogic.futbol.core.operation.Operation
     */
    void onStartOperation();
}