package com.globallogic.futbol.core.analyzers;

import android.content.Intent;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategyAnalyzer;

import java.io.Serializable;

/**
 * A simple implementation of {@link IStrategyAnalyzer}.
 *
 * @author facundo.mengoni
 * @since 0.3.0
 */
public class DefaultAnalyzer<T extends Serializable> implements IStrategyAnalyzer {
    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    protected T mResult;

    public void analyzeException(Exception anException) {
    }

    public void addExtrasForResultError(Intent intent) {
    }

    public void addExtrasForResultOk(Intent intent) {
        intent.putExtra(EXTRA_RESULT, mResult);
    }
}
