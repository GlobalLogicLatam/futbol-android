package com.globallogic.futbol.strategies.ion;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategyHttpAnalyzer;
import com.globallogic.futbol.core.operations.Operation;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpDelete;

public class StrategyIonSingleStringDelete extends StrategyIonSingleString {
    private static final String TAG = StrategyIonSingleStringDelete.class.getSimpleName();

    public StrategyIonSingleStringDelete(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer, String aUrl) {
        super(anOperation, anAnalyzer, aUrl);
    }

    public StrategyIonSingleStringDelete(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer, StrategyIonConfig aStrategyIonConfig, String aUrl) {
        super(anOperation, anAnalyzer, aStrategyIonConfig, aUrl);
    }

    protected String getMethod() {
        return HttpDelete.METHOD_NAME;
    }

    @Override
    public HttpRequest getHttpRequest() {
        return new HttpDelete();
    }
}