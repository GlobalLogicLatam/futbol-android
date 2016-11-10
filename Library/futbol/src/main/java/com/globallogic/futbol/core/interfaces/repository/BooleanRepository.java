package com.globallogic.futbol.core.interfaces.repository;

/**
 * The common behaviour for all {@link Boolean} in the {@link android.content.SharedPreferences}
 *
 * @author facundo.mengoni
 * @since 0.3.4
 */
public interface BooleanRepository extends SharedPreferenceRepository {
    Boolean getBoolean();

    void putBoolean(boolean value);
}