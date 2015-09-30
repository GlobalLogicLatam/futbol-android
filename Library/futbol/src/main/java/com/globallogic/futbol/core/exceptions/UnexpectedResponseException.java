package com.globallogic.futbol.core.exceptions;

/**
 * Created by facundo.mengoni on 9/30/2015.
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
