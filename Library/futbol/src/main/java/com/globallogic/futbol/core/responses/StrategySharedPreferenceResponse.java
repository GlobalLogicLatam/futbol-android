package com.globallogic.futbol.core.responses;

import java.util.Set;

/**
 * A wrapper that contains an object as response of get something from de Shared preference.
 * It represent a specific responses from the Shared preference.
 *
 * @author facundo.mengoni
 * @since 0.3.3
 */
public class StrategySharedPreferenceResponse extends StrategyResponse {
    private Boolean mBoolean;
    private Float mFloat;
    private Integer mInt;
    private Long mLong;
    private String mString;
    private Set<String> mStringSet;

    public StrategySharedPreferenceResponse() {
    }

    public StrategySharedPreferenceResponse(boolean aBoolean) {
        this.mBoolean = aBoolean;
    }


    public StrategySharedPreferenceResponse(float aFloat) {
        this.mFloat = aFloat;

    }

    public StrategySharedPreferenceResponse(int anInt) {
        this.mInt = anInt;
    }

    public StrategySharedPreferenceResponse(long aLong) {
        this.mLong = aLong;
    }

    public StrategySharedPreferenceResponse(String aString) {
        this.mString = aString;
    }

    public StrategySharedPreferenceResponse(Set<String> aStringSet) {
        this.mStringSet = aStringSet;
    }

    public Boolean isBoolean() {
        return mBoolean;
    }

    public void setBoolean(boolean aBoolean) {
        this.mBoolean = aBoolean;
    }

    public Float getFloat() {
        return mFloat;
    }

    public void setFloat(float aFloat) {
        this.mFloat = aFloat;
    }

    public Integer getInt() {
        return mInt;
    }

    public void setInt(int anInt) {
        this.mInt = anInt;
    }

    public Long getLong() {
        return mLong;
    }

    public void setLong(long aLong) {
        this.mLong = aLong;
    }

    public String getString() {
        return mString;
    }

    public void setString(String aString) {
        this.mString = aString;
    }

    public Set<String> getStringSet() {
        return mStringSet;
    }

    public void setStringSet(Set<String> aStringSet) {
        this.mStringSet = aStringSet;
    }

    @Override
    public String toString() {
        return "StrategySharedPreferenceResponse{" +
                "mBoolean=" + mBoolean +
                ", mFloat=" + mFloat +
                ", mInt=" + mInt +
                ", mLong=" + mLong +
                ", mString='" + mString + '\'' +
                ", mStringSet=" + mStringSet +
                '}';
    }
}