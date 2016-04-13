package com.globallogic.futbol.core.analyzers;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategyHttpAnalyzer;

import java.io.Serializable;

/**
 * A simple implementation of {@link IStrategyHttpAnalyzer}.
 *
 * @author facundo.mengoni
 * @since 0.3.0
 */
public abstract class DefaultHttpAnalyzer<T extends Serializable> extends DefaultAnalyzer<T> implements IStrategyHttpAnalyzer {
    /**
     * {@inheritDoc}
     **/
    @Override
    public Boolean analyzeResult(Integer aHttpCode, String result) {
        if (aHttpCode.equals(getExpectedHttpCode())) {
            this.mResult = parseResponse(result);
            return true;
        }
        return false;
    }

    /**
     * It should be the http code where the server will return the response.
     *
     * @return The expected http code.
     */
    protected Integer getExpectedHttpCode() {
        return 200;
    }

    /**
     * Parse the string to {@link T}.
     * @param result The response obtained from the server.
     * @return The {@link T} parsed.
     */
    protected abstract T parseResponse(String result);
}
