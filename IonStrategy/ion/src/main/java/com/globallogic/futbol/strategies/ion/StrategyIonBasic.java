package com.globallogic.futbol.strategies.ion;

import com.globallogic.futbol.core.OperationApp;
import com.globallogic.futbol.core.interfaces.analyzers.IStrategyHttpAnalyzer;
import com.globallogic.futbol.core.operations.Operation;
import com.globallogic.futbol.core.responses.StrategyHttpResponse;
import com.globallogic.futbol.core.strategies.HttpOperationStrategy;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;

import org.apache.http.HttpRequest;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class StrategyIonBasic extends HttpOperationStrategy implements Serializable, FutureCallback<Response<String>> {
    private static final String TAG = StrategyIonBasic.class.getSimpleName();
    private final StrategyIonConfig mConfig;
    private final String mUrl;
    private ArrayList<KeyValue> mHeaders = new ArrayList<>();

    protected StrategyIonBasic(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer, String aUrl) {
        this(anOperation, anAnalyzer, StrategyIonConfig.getDefaultConfig(), aUrl);
    }

    protected StrategyIonBasic(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer, StrategyIonConfig aStrategyIonConfig, String aUrl) {
        super(anOperation, anAnalyzer);
        this.mConfig = aStrategyIonConfig;
        this.mUrl = aUrl;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public void addHeader(KeyValue valuePair) {
        mHeaders.add(valuePair);
    }

    @Override
    protected void doRequestImpl() {
        Builders.Any.B b = Ion
                .with(OperationApp.getInstance())
                .load(getMethod(), getUrl())
                .setTimeout(mConfig.getTimeOutMillisecond())
                .addHeader("Content-Length", "0");
        for (KeyValue keyValue : mHeaders)
            b.addHeader(keyValue.getKey(), keyValue.getValue());
        getResponse(b);
    }

    public abstract HttpRequest getHttpRequest();

    protected abstract String getMethod();

    protected abstract void getResponse(Builders.Any.B b);

    @Override
    public void onCompleted(final Exception e, final Response<String> response) {
        StrategyHttpResponse strategyResponse = new StrategyHttpResponse();
        int code = -1;
        String result = "";
        if (response != null) {
            code = response.getHeaders().code();
            result = response.getResult();
        }
        strategyResponse.setHttpCode(code);
        strategyResponse.setResponse(result);
        parseResponse(e, strategyResponse);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StrategyIonBasic that = (StrategyIonBasic) o;

        if (mHeaders != null ? !mHeaders.equals(that.mHeaders) : that.mHeaders != null)
            return false;
        if (!mUrl.equals(that.mUrl)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mUrl.hashCode();
        result = 31 * result + (mHeaders != null ? mHeaders.hashCode() : 0);
        return result;
    }
}