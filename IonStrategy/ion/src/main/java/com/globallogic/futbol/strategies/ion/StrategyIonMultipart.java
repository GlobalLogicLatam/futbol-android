package com.globallogic.futbol.strategies.ion;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategyHttpAnalyzer;
import com.globallogic.futbol.core.operations.Operation;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;

import java.util.ArrayList;

public abstract class StrategyIonMultipart extends StrategyIonBasic {
    private static final String TAG = StrategyIonMultipart.class.getSimpleName();
    private ArrayList<Part> mArrayList;

    public StrategyIonMultipart(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer, String aUrl, ArrayList<Part> mArrayList) {
        super(anOperation, anAnalyzer, aUrl);
        this.mArrayList = mArrayList;
    }

    public StrategyIonMultipart(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer, StrategyIonConfig aStrategyIonConfig, String aUrl, ArrayList<Part> mArrayList) {
        super(anOperation, anAnalyzer, aStrategyIonConfig, aUrl);
        this.mArrayList = mArrayList;
    }

    public ArrayList<Part> getArrayList() {
        return mArrayList;
    }

    public void setArrayList(ArrayList<Part> anArrayList) {
        this.mArrayList = anArrayList;
    }

    @Override
    protected Future<Response<String>> getResponse(Builders.Any.B b) {
        return b.addMultipartParts(mArrayList).asString().withResponse().setCallback(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StrategyIonMultipart)) return false;
        if (!super.equals(o)) return false;

        StrategyIonMultipart that = (StrategyIonMultipart) o;

        if (mArrayList != null ? !mArrayList.equals(that.mArrayList) : that.mArrayList != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (mArrayList != null ? mArrayList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StrategyIonMultipart{" +
                "mArrayList=" + mArrayList +
                '}';
    }
}