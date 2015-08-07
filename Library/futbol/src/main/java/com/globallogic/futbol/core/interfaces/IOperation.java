package com.globallogic.futbol.core.interfaces;

import android.content.Context;

public interface IOperation {
    /**
     * It registers that the operation would be executed but for some reason could not be implemented and that there is someone waiting to do.
     */
    IOperation setAsWaiting();

    /**
     * Executes the operation with the specified parameters.
     *
     * @param arg The arguments of the operation.
     */
    boolean performOperation(Object... arg);

    /**
     * This allows you to analyze the http code and save the objects according to code.
     * Is triggered only if you can connect to the server.
     */
    void analyzeResult(int aHttpCode, String result);

    /**
     * This allows you to analyze the exception occurred.
     * Is triggered only if an error has occurred.
     */
    void analyzeException(Exception e);

    /**
     * This should send the message.
     * Is triggered only if you can connect to the server.
     */
    void sendBroadcastForOk();

    /**
     * This should send the message.
     * Is triggered only if an error has occurred.
     */
    void sendBroadcastForError();

    /**
     * You must return a string for display in the UI considering the i18n of texts.
     */
    String getError(Context aContext);
}