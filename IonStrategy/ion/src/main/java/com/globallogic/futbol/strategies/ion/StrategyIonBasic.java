package com.globallogic.futbol.strategies.ion;

import android.annotation.TargetApi;
import android.os.Build;

import com.globallogic.futbol.core.OperationApp;
import com.globallogic.futbol.core.interfaces.IOperationStrategy;
import com.globallogic.futbol.core.interfaces.IStrategyCallback;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;

import org.apache.http.HttpRequest;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class StrategyIonBasic implements Serializable, IOperationStrategy, FutureCallback<Response<String>> {
    private static final String TAG = StrategyIonBasic.class.getSimpleName();
    private final StrategyIonConfig mConfig;
    private final String mUrl;
    private ArrayList<KeyValue> mHeaders = new ArrayList<>();
    protected IStrategyCallback mCallback;

    protected StrategyIonBasic(String aUrl) {
        this(StrategyIonConfig.getDefaultConfig(), aUrl);
    }

    protected StrategyIonBasic(StrategyIonConfig aStrategyIonConfig, String aUrl) {
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
    public void updateCallback(IStrategyCallback callback) {
        this.mCallback = callback;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void doRequest(IStrategyCallback callback) {
        this.mCallback = callback;
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
        int code = -1;
        String result = "";
        if (response != null) {
            code = response.getHeaders().code();
            result = response.getResult();
        }
        mCallback.parseResponse(e, code, result);
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