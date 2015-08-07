package com.globallogic.futbol.strategies.ion;

import com.koushikdutta.ion.builder.Builders;

import java.io.File;

public abstract class StrategyIonSingleFile extends StrategyIonBasic {
    private static final String TAG = StrategyIonSingleFile.class.getSimpleName();
    private final File mFile;

    public StrategyIonSingleFile(String aUrl, File mFile) {
        super(aUrl);
        this.mFile = mFile;
    }

    public StrategyIonSingleFile(StrategyIonConfig aStrategyIonConfig, String aUrl, File mFile) {
        super(aStrategyIonConfig, aUrl);
        this.mFile = mFile;
    }

    @Override
    protected void getResponse(Builders.Any.B b) {
        b.setFileBody(mFile).asString().withResponse().setCallback(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StrategyIonSingleFile)) return false;
        if (!super.equals(o)) return false;

        StrategyIonSingleFile that = (StrategyIonSingleFile) o;

        if (mFile != null ? !mFile.equals(that.mFile) : that.mFile != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (mFile != null ? mFile.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StrategyIonSingleFile{" +
                "mFile=" + mFile +
                '}';
    }
}