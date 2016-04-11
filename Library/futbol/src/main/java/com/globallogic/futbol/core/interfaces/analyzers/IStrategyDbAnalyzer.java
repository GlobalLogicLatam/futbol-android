package com.globallogic.futbol.core.interfaces.analyzers;

import android.content.Intent;

/**
 * A concrete interface for analyze a db strategy.
 *
 * @author facundo.mengoni
 * @since 0.3.0
 */
public interface IStrategyDbAnalyzer<T> extends IStrategyAnalyzer {
    /**
     * Analyze the response.
     *
     * @param aResponse The response obtained from the DB.
     * @return true if {@link T} is an expected result, false in other case.
     * @see #addExtrasForResultOk(Intent)
     */
    Boolean analyzeResult(T aResponse);
}