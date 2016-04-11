package com.globallogic.futbol.core.interfaces;

/**
 * @author facundo.mengoni
 * @since 0.1.0
 */
public interface IOperation {
    /**
     * Executes the operation with the specified parameters.
     *
     * @param arg The arguments of the operation.
     * @return true if was executed some work, false in other case.
     */
    boolean performOperation(Object... arg);

    /**
     * Notify that the operation was started
     */
    void sendBroadcastForStart();

    /**
     * Notify that the operation was finished
     */
    void sendBroadcastForFinish();
}