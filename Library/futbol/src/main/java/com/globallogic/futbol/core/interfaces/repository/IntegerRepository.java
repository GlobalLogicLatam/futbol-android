package com.globallogic.futbol.core.interfaces.repository;

/**
 * The common behaviour for all {@link Integer} in the {@link android.content.SharedPreferences}
 *
 * @author facundo.mengoni
 * @since 0.3.4
 */
public interface IntegerRepository {
    Boolean hasKey();

    Integer getInteger();

    void putInteger(int value);
}
