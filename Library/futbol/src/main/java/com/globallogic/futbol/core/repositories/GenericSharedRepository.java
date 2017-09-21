package com.globallogic.futbol.core.repositories;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;

import com.globallogic.futbol.core.interfaces.repository.BooleanRepository;
import com.globallogic.futbol.core.interfaces.repository.FloatRepository;
import com.globallogic.futbol.core.interfaces.repository.IntegerRepository;
import com.globallogic.futbol.core.interfaces.repository.LongRepository;
import com.globallogic.futbol.core.interfaces.repository.StringRepository;
import com.globallogic.futbol.core.interfaces.repository.StringSetRepository;

import java.util.Set;

/**
 * A simple class that helps you with the repositories of shared preferences.
 *
 * @author facundo.mengoni
 * @since 0.3.4
 */
public abstract class GenericSharedRepository implements FloatRepository, IntegerRepository, LongRepository, StringRepository, StringSetRepository, BooleanRepository {
    //region Default values
    private static final boolean DEFAULT_BOOLEAN_VALUE = false;
    private static final float DEFAULT_FLOAT_VALUE = -1;
    private static final int DEFAULT_INTEGER_VALUE = -1;
    private static final long DEFAULT_LONG_VALUE = -1;
    private static final String DEFAULT_STRING = null;
    private static final Set<String> DEFAULT_STRING_SET = null;
    //endregion

    //region Variables
    SharedPreferences mShared;
    //endregion

    //region Constructors
    public GenericSharedRepository(SharedPreferences shared) {
        this.mShared = shared;
    }
    //endregion

    protected abstract String getKey();

    //region Private methods implementation
    private SharedPreferences.Editor getEditor() {
        return mShared.edit();
    }

    private void apply(SharedPreferences.Editor editor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }
    //endregion

    //region CommonRepository implementation
    @Override
    public Boolean hasKey() {
        return mShared.contains(getKey());
    }

    @Override
    public void removeKey() {
        apply(getEditor().remove(getKey()));
    }
    //endregion

    //region FloatRepository implementation
    @Override
    public Boolean getBoolean() {
        return mShared.getBoolean(getKey(), DEFAULT_BOOLEAN_VALUE);
    }

    @Override
    public void putBoolean(boolean value) {
        apply(getEditor().putBoolean(getKey(), value));
    }
    //endregion

    //region FloatRepository implementation
    @Override
    public Float getFloat() {
        return mShared.getFloat(getKey(), DEFAULT_FLOAT_VALUE);
    }

    @Override
    public void putFloat(float value) {
        apply(getEditor().putFloat(getKey(), value));
    }
    //endregion

    //region LongRepository implementation
    @Override
    public Integer getInteger() {
        return mShared.getInt(getKey(), DEFAULT_INTEGER_VALUE);
    }

    @Override
    public void putInteger(int value) {
        apply(getEditor().putInt(getKey(), value));
    }
    //endregion

    //region LongRepository implementation
    @Override
    public Long getLong() {
        return mShared.getLong(getKey(), DEFAULT_LONG_VALUE);
    }

    @Override
    public void putLong(long value) {
        apply(getEditor().putLong(getKey(), value));
    }
    //endregion

    //region StringRepository implementation
    @Override
    public String getString() {
        return mShared.getString(getKey(), DEFAULT_STRING);
    }

    @Override
    public void putString(String value) {
        apply(getEditor().putString(getKey(), value));
    }
    //endregion

    //region StringSetRepository implementation
    @Override
    public Set<String> getStringSet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return mShared.getStringSet(getKey(), DEFAULT_STRING_SET);
        }
        return DEFAULT_STRING_SET;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void putStringSet(Set<String> value) {
        apply(getEditor().putStringSet(getKey(), value));
    }
    //endregion
}