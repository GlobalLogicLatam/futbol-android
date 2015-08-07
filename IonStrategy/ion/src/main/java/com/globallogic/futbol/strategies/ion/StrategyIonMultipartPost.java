package com.globallogic.futbol.strategies.ion;

import com.koushikdutta.async.http.body.Part;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;

import java.util.ArrayList;

public class StrategyIonMultipartPost extends StrategyIonMultipart {
    private static final String TAG = StrategyIonMultipartPost.class.getSimpleName();

    public StrategyIonMultipartPost(StrategyIonConfig aStrategyIonConfig, String aUrl, ArrayList<Part> mArrayList) {
        super(aStrategyIonConfig, aUrl, mArrayList);
    }

    @Override
    public HttpRequest getHttpRequest() {
        return new HttpPost();
    }


    protected String getMethod() {
        return HttpPost.METHOD_NAME;
    }
}