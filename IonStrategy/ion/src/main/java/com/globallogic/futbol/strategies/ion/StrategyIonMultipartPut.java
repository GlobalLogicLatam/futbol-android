package com.globallogic.futbol.strategies.ion;

import com.koushikdutta.async.http.body.Part;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPut;

import java.util.ArrayList;

public class StrategyIonMultipartPut extends StrategyIonMultipart {
    private static final String TAG = StrategyIonMultipartPut.class.getSimpleName();

    public StrategyIonMultipartPut(String aUrl, ArrayList<Part> mArrayList) {
        super(aUrl, mArrayList);
    }


    public StrategyIonMultipartPut(StrategyIonConfig aStrategyIonConfig, String aUrl, ArrayList<Part> mArrayList) {
        super(aStrategyIonConfig, aUrl, mArrayList);
    }

    @Override
    public HttpRequest getHttpRequest() {
        return new HttpPut();
    }


    protected String getMethod() {
        return HttpPut.METHOD_NAME;
    }
}