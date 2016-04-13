package com.globallogic.futbol.core.interfaces.parsers;

import com.globallogic.futbol.core.responses.StrategyHttpResponse;

/**
 * A specific interface to work with the response in an strategy of http
 *
 * @author facundo.mengoni
 * @see StrategyHttpResponse
 * @since 0.3.0
 */
public interface IOperationHttpParser<T extends StrategyHttpResponse> extends IOperationParser<T> {
    /**
     * Do the hard work here to analyze the different cases. It should be executed outside the thread UI.
     *
     * @param anException The exception occurred.
     * @param aHttpCode   The http code obtained
     * @param aString     The http string obtained
     * @return true if is an http code expected and can parse the string, false in other case.
     * @see #afterWorkInBackground(Boolean)
     */
    Boolean workInBackground(Exception anException, Integer aHttpCode, String aString);

    /**
     * Notify that the strategy has been finished. It should be executed in the thread UI.
     *
     * @param aResult Is the result of {@link #workInBackground(Exception, Integer, String)}.
     */
    void afterWorkInBackground(Boolean aResult);
}