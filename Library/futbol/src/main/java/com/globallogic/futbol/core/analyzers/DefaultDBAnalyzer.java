package com.globallogic.futbol.core.analyzers;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategyDbAnalyzer;

import java.io.Serializable;

/**
 * A simple implementation of {@link IStrategyDbAnalyzer}.
 *
 * @author facundo.mengoni
 * @since 0.3.0
 */
public class DefaultDBAnalyzer<T extends Serializable> extends DefaultAnalyzer implements IStrategyDbAnalyzer<T> {
    /**
     * {@inheritDoc}
     **/
    @Override
    public Boolean analyzeResult(T aResponse) {
        mResult = aResponse;
        return true;
    }
}
