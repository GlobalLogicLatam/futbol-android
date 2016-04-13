package com.globallogic.futbol.strategies.ion;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategyHttpAnalyzer;
import com.globallogic.futbol.core.operations.Operation;
import com.koushikdutta.async.http.body.Part;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;

import java.util.ArrayList;

public class StrategyIonMultipartPost extends StrategyIonMultipart {
    private static final String TAG = StrategyIonMultipartPost.class.getSimpleName();

    public StrategyIonMultipartPost(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer, StrategyIonConfig aStrategyIonConfig, String aUrl, ArrayList<Part> mArrayList) {
        super(anOperation, anAnalyzer, aStrategyIonConfig, aUrl, mArrayList);
    }

    @Override
    public HttpRequest getHttpRequest() {
        return new HttpPost();
    }


    protected String getMethod() {
        return HttpPost.METHOD_NAME;
    }
}