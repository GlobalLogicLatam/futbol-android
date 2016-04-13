package com.globallogic.futbol.strategies.ion;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategyHttpAnalyzer;
import com.globallogic.futbol.core.operations.Operation;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;

public class StrategyIonSingleStringPost extends StrategyIonSingleString {
    private static final String TAG = StrategyIonSingleStringPost.class.getSimpleName();

    public StrategyIonSingleStringPost(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer, String aUrl) {
        super(anOperation, anAnalyzer, aUrl);
    }

    public StrategyIonSingleStringPost(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer,String aUrl, String aString) {
        super(anOperation, anAnalyzer, aUrl, aString);
    }
    public StrategyIonSingleStringPost(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer,StrategyIonConfig aStrategyIonConfig, String aUrl) {
        super(anOperation, anAnalyzer, aStrategyIonConfig, aUrl);
    }

    public StrategyIonSingleStringPost(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer,StrategyIonConfig aStrategyIonConfig, String aUrl, String aString) {
        super(anOperation, anAnalyzer, aStrategyIonConfig, aUrl, aString);
    }

    @Override
    public HttpRequest getHttpRequest() {
        return new HttpPost();
    }


    protected String getMethod() {
        return HttpPost.METHOD_NAME;
    }
}