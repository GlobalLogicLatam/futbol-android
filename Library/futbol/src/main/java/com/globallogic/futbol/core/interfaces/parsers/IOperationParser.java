package com.globallogic.futbol.core.interfaces.parsers;

import com.globallogic.futbol.core.responses.StrategyResponse;

/**
 * A basic interface to parse a response in an strategy
 *
 * @author facundo.mengoni
 * @see StrategyResponse
 * @since 0.3.0
 */
public interface IOperationParser<T extends StrategyResponse> {
    /**
     * Parse the response and do something with that to analyze the different cases.
     *
     * @param anException       The exception occurred.
     * @param aStrategyResponse The {@link StrategyResponse} obtained.
     */
    void parseResponse(Exception anException, T aStrategyResponse);
}