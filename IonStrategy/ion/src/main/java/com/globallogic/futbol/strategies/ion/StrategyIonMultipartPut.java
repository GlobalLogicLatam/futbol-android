package com.globallogic.futbol.strategies.ion;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategyHttpAnalyzer;
import com.globallogic.futbol.core.operations.Operation;
import com.koushikdutta.async.http.body.Part;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPut;

import java.util.ArrayList;

public class StrategyIonMultipartPut extends StrategyIonMultipart {
    private static final String TAG = StrategyIonMultipartPut.class.getSimpleName();

    public StrategyIonMultipartPut(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer, String aUrl, ArrayList<Part> mArrayList) {
        super(anOperation, anAnalyzer, aUrl, mArrayList);
    }


    public StrategyIonMultipartPut(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer, StrategyIonConfig aStrategyIonConfig, String aUrl, ArrayList<Part> mArrayList) {
        super(anOperation, anAnalyzer, aStrategyIonConfig, aUrl, mArrayList);
    }

    @Override
    public HttpRequest getHttpRequest() {
        return new HttpPut();
    }


    protected String getMethod() {
        return HttpPut.METHOD_NAME;
    }
}