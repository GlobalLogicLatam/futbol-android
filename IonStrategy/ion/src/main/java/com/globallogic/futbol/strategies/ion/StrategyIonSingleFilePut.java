package com.globallogic.futbol.strategies.ion;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPut;

import java.io.File;

public class StrategyIonSingleFilePut extends StrategyIonSingleFile {
    private static final String TAG = StrategyIonSingleFilePut.class.getSimpleName();

    public StrategyIonSingleFilePut(String aUrl, File mFile) {
        super(aUrl, mFile);
    }

    public StrategyIonSingleFilePut(StrategyIonConfig aStrategyIonConfig, String aUrl, File mFile) {
        super(aStrategyIonConfig, aUrl, mFile);
    }

    @Override
    public HttpRequest getHttpRequest() {
        return new HttpPut();
    }


    protected String getMethod() {
        return HttpPut.METHOD_NAME;
    }
}