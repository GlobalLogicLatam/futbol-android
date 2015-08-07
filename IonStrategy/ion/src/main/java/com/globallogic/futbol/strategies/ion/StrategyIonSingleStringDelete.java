package com.globallogic.futbol.strategies.ion;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpDelete;

public class StrategyIonSingleStringDelete extends StrategyIonSingleString {
    private static final String TAG = StrategyIonSingleStringDelete.class.getSimpleName();

    public StrategyIonSingleStringDelete(String aUrl) {
        super(aUrl);
    }

    public StrategyIonSingleStringDelete(StrategyIonConfig aStrategyIonConfig, String aUrl) {
        super(aStrategyIonConfig, aUrl);
    }

    protected String getMethod() {
        return HttpDelete.METHOD_NAME;
    }

    @Override
    public HttpRequest getHttpRequest() {
        return new HttpDelete();
    }
}