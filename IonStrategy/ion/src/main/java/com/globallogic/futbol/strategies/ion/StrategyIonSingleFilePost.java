package com.globallogic.futbol.strategies.ion;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategyHttpAnalyzer;
import com.globallogic.futbol.core.operations.Operation;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;

import java.io.File;

public class StrategyIonSingleFilePost extends StrategyIonSingleFile {
    private static final String TAG = StrategyIonSingleFilePost.class.getSimpleName();

    public StrategyIonSingleFilePost(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer, String aUrl, File mFile) {
        super(anOperation, anAnalyzer, aUrl, mFile);
    }

    public StrategyIonSingleFilePost(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer,StrategyIonConfig aStrategyIonConfig, String aUrl, File mFile) {
        super(anOperation, anAnalyzer, aStrategyIonConfig, aUrl, mFile);
    }

    @Override
    public HttpRequest getHttpRequest() {
        return new HttpPost();
    }


    protected String getMethod() {
        return HttpPost.METHOD_NAME;
    }
}