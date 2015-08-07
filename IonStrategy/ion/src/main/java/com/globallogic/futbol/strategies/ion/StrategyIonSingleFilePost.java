package com.globallogic.futbol.strategies.ion;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;

import java.io.File;

public class StrategyIonSingleFilePost extends StrategyIonSingleFile {
    private static final String TAG = StrategyIonSingleFilePost.class.getSimpleName();

    public StrategyIonSingleFilePost(String aUrl, File mFile) {
        super(aUrl, mFile);
    }

    public StrategyIonSingleFilePost(StrategyIonConfig aStrategyIonConfig, String aUrl, File mFile) {
        super(aStrategyIonConfig, aUrl, mFile);
    }

    @Override
    public HttpRequest getHttpRequest() {
        return new HttpPost();
    }


    protected String getMethod() {
        return HttpPost.METHOD_NAME;
    }
}