package com.globallogic.futbol.core.interfaces.analyzers;

import android.content.Intent;

/**
 * A basic interface for analyze the strategies.
 *
 * @author facundo.mengoni
 * @since 0.3.0
 */
public interface IStrategyAnalyzer {
    /**
     * Add the extras that you need to notify what happened.
     *
     * @param intent The intent where add the extras.
     * @see #analyzeException(Exception)
     */
    void addExtrasForResultError(Intent intent);

    /**
     * Analyze what you want to do if occurs an exception.
     *
     * @param anException The exception ocurred.
     * @see #addExtrasForResultError(Intent)
     */
    void analyzeException(Exception anException);

    /**
     * Add the extras that you need to notify the response.
     *
     * @param intent The intent where add the extras.
     * @see IStrategyDbAnalyzer#analyzeResult(Object)
     * @see IStrategyHttpAnalyzer#analyzeResult(Integer, String)
     */
    void addExtrasForResultOk(Intent intent);
}