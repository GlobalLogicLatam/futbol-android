package com.globallogic.futbol.core.interfaces;

/**
 * @author facundo.mengoni
 * @since 0.1.0
 */
public interface IStrategy {
    /**
     * Executes the strategy.
     */
    void execute();

    /**
     * Notify that the strategy return an ok.
     */
    void sendBroadcastForOk();

    /**
     * Notify that the strategy return an error.
     */
    void sendBroadcastForError();
}