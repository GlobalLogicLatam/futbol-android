package com.globallogic.futbol.core.interfaces;

public interface IStrategyCallback {
    /**
     * Analysis of the response returned by the server
     *
     * @param aException the exception thrown because of some error
     * @param aHttpCode  the http response code
     * @param aString    the string of the response
     */
    void parseResponse(Exception aException, int aHttpCode, String aString);
}