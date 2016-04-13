package com.globallogic.futbol.core.interfaces.parsers;

import com.globallogic.futbol.core.responses.StrategyDbResponse;

/**
 * A specific interface to work with the response in an strategy of db
 *
 * @author facundo.mengoni
 * @see StrategyDbResponse
 * @since 0.3.0
 */
public interface IOperationDbParser<U, T extends StrategyDbResponse> extends IOperationParser<T> {
    /**
     * Do the hard work here to analyze the different cases. It should be executed outside the thread UI.
     *
     * @param anException The exception occurred.
     * @param aResponse   The {@link U} obtained
     * @return true if {@link U} is an expected result, false in other case.
     * @see #afterWorkInBackground(Boolean)
     */
    Boolean workInBackground(Exception anException, U aResponse);

    /**
     * Notify that the strategy has been finished. It should be executed in the thread UI.
     *
     * @param aResult Is the result of {@link #workInBackground(Exception, U)}.
     */
    void afterWorkInBackground(Boolean aResult);
}