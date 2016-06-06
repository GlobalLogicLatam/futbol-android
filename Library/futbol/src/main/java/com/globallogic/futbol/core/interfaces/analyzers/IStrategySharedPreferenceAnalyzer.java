package com.globallogic.futbol.core.interfaces.analyzers;

import android.content.Intent;

/**
 * A concrete interface for analyze a shared preference strategy.
 *
 * @author facundo.mengoni
 * @since 0.3.3
 */
public interface IStrategySharedPreferenceAnalyzer<T> extends IStrategyAnalyzer {
    /**
     * Analyze the response.
     *
     * @param aResponse The response obtained from the SharedPreference.
     * @return true if {@link T} is an expected result, false in other case.
     * @see #addExtrasForResultOk(Intent)
     */
    Boolean analyzeResult(T aResponse);
}