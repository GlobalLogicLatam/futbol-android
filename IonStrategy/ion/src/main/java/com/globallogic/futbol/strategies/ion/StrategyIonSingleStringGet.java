package com.globallogic.futbol.strategies.ion;

import android.text.TextUtils;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategyHttpAnalyzer;
import com.globallogic.futbol.core.operations.Operation;

import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StrategyIonSingleStringGet extends StrategyIonSingleString {
    private static final String TAG = StrategyIonSingleStringGet.class.getSimpleName();

    public static Logger mLogger;

    static {
        mLogger = Logger.getLogger(TAG);
        mLogger.setLevel(Level.OFF);
    }

    private final ArrayList<NameValuePair> mParams = new ArrayList<NameValuePair>();

    public StrategyIonSingleStringGet(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer,String aUrl) {
        super(anOperation, anAnalyzer, aUrl);
    }

    public StrategyIonSingleStringGet(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer, String aUrl, String aString) {
        super(anOperation, anAnalyzer, aUrl, aString);
    }

    public StrategyIonSingleStringGet(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer,StrategyIonConfig aStrategyIonConfig, String aUrl) {
        super(anOperation, anAnalyzer, aStrategyIonConfig, aUrl);
    }

    public StrategyIonSingleStringGet(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer,StrategyIonConfig aStrategyIonConfig, String aUrl, String aString) {
        super(anOperation, anAnalyzer, aStrategyIonConfig, aUrl, aString);
    }

    protected static String formatUrl(String aUrl, ArrayList<NameValuePair> aParams) {
        String finalUrl;
        String params = null;
        try {
            if (!aParams.isEmpty()) {
                params = URLEncodedUtils.format(aParams, "utf-8");
            }
        } catch (Exception e) {
            mLogger.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            if (!TextUtils.isEmpty(params)) {
                finalUrl = aUrl.concat("?" + params);
            } else {
                finalUrl = aUrl;
            }
        }
        return finalUrl;
    }

    protected String getMethod() {
        return HttpGet.METHOD_NAME;
    }

    @Override
    public String getUrl() {
        return formatUrl(super.getUrl(), getParams());
    }

    @Override
    public HttpRequest getHttpRequest() {
        return new HttpGet();
    }

    public ArrayList<NameValuePair> getParams() {
        return mParams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StrategyIonSingleStringGet)) return false;
        if (!super.equals(o)) return false;

        StrategyIonSingleStringGet that = (StrategyIonSingleStringGet) o;

        if (!mParams.equals(that.mParams)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + mParams.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "StrategyGet{" +
                "mParams=" + mParams +
                "} " + super.toString();
    }
}