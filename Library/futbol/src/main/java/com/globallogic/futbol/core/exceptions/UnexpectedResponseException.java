package com.globallogic.futbol.core.exceptions;

import com.globallogic.futbol.core.strategies.DbOperationStrategy;
import com.globallogic.futbol.core.strategies.HttpOperationStrategy;

/**
 * Exception thrown when a response is unexpected and the strategy don't know how analyze it.
 *
 * @author facundo.mengoni
 * @see DbOperationStrategy#workInBackground(Exception, Object)
 * @see HttpOperationStrategy#workInBackground(Exception, Integer, String)
 * @since 0.1.0
 */
public class UnexpectedResponseException extends Exception {
    public UnexpectedResponseException() {
    }

    public UnexpectedResponseException(String detailMessage) {
        super(detailMessage);
    }

    public UnexpectedResponseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UnexpectedResponseException(Throwable throwable) {
        super(throwable);
    }
}
