package com.globallogic.futbol.strategies.ion;

import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.ion.builder.Builders;

import java.util.ArrayList;

public abstract class StrategyIonMultipart extends StrategyIonBasic {
    private static final String TAG = StrategyIonMultipart.class.getSimpleName();
    private final ArrayList<Part> mArrayList;

    public StrategyIonMultipart(String aUrl, ArrayList<Part> mArrayList) {
        super(aUrl);
        this.mArrayList = mArrayList;
    }

    public StrategyIonMultipart(StrategyIonConfig aStrategyIonConfig, String aUrl, ArrayList<Part> mArrayList) {
        super(aStrategyIonConfig, aUrl);
        this.mArrayList = mArrayList;
    }

    @Override
    protected void getResponse(Builders.Any.B b) {
        b.addMultipartParts(mArrayList).asString().withResponse().setCallback(this);
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