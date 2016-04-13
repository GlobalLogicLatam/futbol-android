package com.globallogic.futbol.strategies.ion;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategyHttpAnalyzer;
import com.globallogic.futbol.core.operations.Operation;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPut;

import java.io.File;

public class StrategyIonSingleFilePut extends StrategyIonSingleFile {
    private static final String TAG = StrategyIonSingleFilePut.class.getSimpleName();

    public StrategyIonSingleFilePut(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer, String aUrl, File mFile) {
        super(anOperation, anAnalyzer, aUrl, mFile);
    }

    public StrategyIonSingleFilePut(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer,StrategyIonConfig aStrategyIonConfig, String aUrl, File mFile) {
        super(anOperation, anAnalyzer, aStrategyIonConfig, aUrl, mFile);
    }

    @Override
    public HttpRequest getHttpRequest() {
        return new HttpPut();
    }


    protected String getMethod() {
        return HttpPut.METHOD_NAME;
    }
}