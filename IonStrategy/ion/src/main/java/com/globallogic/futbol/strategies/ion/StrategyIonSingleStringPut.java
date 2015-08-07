package com.globallogic.futbol.strategies.ion;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPut;

public class StrategyIonSingleStringPut extends StrategyIonSingleString {
    private static final String TAG = StrategyIonSingleStringPut.class.getSimpleName();

    public StrategyIonSingleStringPut(String aUrl) {
        super(aUrl);
    }

    public StrategyIonSingleStringPut(String aUrl, String aString) {
        super(aUrl, aString);
    }

    public StrategyIonSingleStringPut(StrategyIonConfig aStrategyIonConfig, String aUrl) {
        super(aStrategyIonConfig, aUrl);
    }

    public StrategyIonSingleStringPut(StrategyIonConfig aStrategyIonConfig, String aUrl, String aString) {
        super(aStrategyIonConfig, aUrl, aString);
    }

    @Override
    public HttpRequest getHttpRequest() {
        return new HttpPut();
    }


    protected String getMethod() {
        return HttpPut.METHOD_NAME;
    }
}