package com.globallogic.futbol.core.interfaces.analyzers;

import android.content.Intent;

/**
 * A concrete interface for analyze a http strategy.
 *
 * @author facundo.mengoni
 * @since 0.3.0
 */
public interface IStrategyHttpAnalyzer extends IStrategyAnalyzer {
    /**
     * Analyze the response.
     *
     * @param aHttpCode The http code obtained
     * @param aString   The http string obtained
     * @return true if is an http code expected and can parse the string, false in other case.
     * @see #addExtrasForResultOk(Intent)
     */
    Boolean analyzeResult(Integer aHttpCode, String aString);
}