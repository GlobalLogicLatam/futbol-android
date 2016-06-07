package com.globallogic.futbol.core.interfaces.repository;

/**
 * The common behaviour for all {@link String} in the {@link android.content.SharedPreferences}
 *
 * @author facundo.mengoni
 * @since 0.3.4
 */
public interface StringRepository {
    Boolean hasKey();

    String getString();

    void putString(String value);
}
