package com.globallogic.futbol.strategies.ion;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;

public class StrategyIonSingleStringPost extends StrategyIonSingleString {
    private static final String TAG = StrategyIonSingleStringPost.class.getSimpleName();

    public StrategyIonSingleStringPost(String aUrl) {
        super(aUrl);
    }

    public StrategyIonSingleStringPost(String aUrl, String aString) {
        super(aUrl, aString);
    }
    public StrategyIonSingleStringPost(StrategyIonConfig aStrategyIonConfig, String aUrl) {
        super(aStrategyIonConfig, aUrl);
    }

    public StrategyIonSingleStringPost(StrategyIonConfig aStrategyIonConfig, String aUrl, String aString) {
        super(aStrategyIonConfig, aUrl, aString);
    }

    @Override
    public HttpRequest getHttpRequest() {
        return new HttpPost();
    }


    protected String getMethod() {
        return HttpPost.METHOD_NAME;
    }
}