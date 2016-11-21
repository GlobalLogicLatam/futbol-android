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

    @Override
    public void analyzeException(Exception anException) {
        // Nothing to do
    }

    @Override
    public void reset() {
        // Nothing to do

    }

    @Override
    public void addExtrasForResultError(Intent intent) {
        // Nothing to do
    }

    @Override
    public void addExtrasForResultOk(Intent intent) {
        intent.putExtra(EXTRA_RESULT, mResult);
    }
}
