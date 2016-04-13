package com.globallogic.futbol.strategies.ion;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategyHttpAnalyzer;
import com.globallogic.futbol.core.operations.Operation;
import com.koushikdutta.ion.builder.Builders;


public abstract class StrategyIonSingleString extends StrategyIonBasic {
    private static final String TAG = StrategyIonSingleString.class.getSimpleName();
    private final String mString;

    public StrategyIonSingleString(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer, String aUrl) {
        super(anOperation, anAnalyzer, aUrl);
        mString = "";
    }

    public StrategyIonSingleString(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer, String aUrl, String aString) {
        super(anOperation, anAnalyzer, aUrl);
        this.mString = aString;
    }

    public StrategyIonSingleString(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer, StrategyIonConfig aStrategyIonConfig, String aUrl) {
        super(anOperation, anAnalyzer, aStrategyIonConfig, aUrl);
        mString = "";
    }

    public StrategyIonSingleString(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer, StrategyIonConfig aStrategyIonConfig, String aUrl, String aString) {
        super(anOperation, anAnalyzer, aStrategyIonConfig, aUrl);
        this.mString = aString;
    }

    @Override
    protected void getResponse(Builders.Any.B b) {
        b.setStringBody(mString).asString().withResponse().setCallback(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StrategyIonSingleString)) return false;
        if (!super.equals(o)) return false;

        StrategyIonSingleString that = (StrategyIonSingleString) o;

        if (mString != null ? !mString.equals(that.mString) : that.mString != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (mString != null ? mString.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StrategyIonSingleString{" +
                "mString='" + mString + '\'' +
                '}';
    }
}