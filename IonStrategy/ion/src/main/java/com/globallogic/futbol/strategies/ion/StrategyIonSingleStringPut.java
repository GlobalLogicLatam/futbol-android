package com.globallogic.futbol.strategies.ion;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategyHttpAnalyzer;
import com.globallogic.futbol.core.operations.Operation;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPut;

public class StrategyIonSingleStringPut extends StrategyIonSingleString {
    private static final String TAG = StrategyIonSingleStringPut.class.getSimpleName();

    public StrategyIonSingleStringPut(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer, String aUrl) {
        super(anOperation, anAnalyzer, aUrl);
    }

    public StrategyIonSingleStringPut(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer, String aUrl, String aString) {
        super(anOperation, anAnalyzer, aUrl, aString);
    }

    public StrategyIonSingleStringPut(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer, StrategyIonConfig aStrategyIonConfig, String aUrl) {
        super(anOperation, anAnalyzer, aStrategyIonConfig, aUrl);
    }

    public StrategyIonSingleStringPut(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer, StrategyIonConfig aStrategyIonConfig, String aUrl, String aString) {
        super(anOperation, anAnalyzer, aStrategyIonConfig, aUrl, aString);
    }

    @Override
    public HttpRequest getHttpRequest() {
        return new HttpPut();
    }


    protected String getMethod() {
        return HttpPut.METHOD_NAME;
    }
}