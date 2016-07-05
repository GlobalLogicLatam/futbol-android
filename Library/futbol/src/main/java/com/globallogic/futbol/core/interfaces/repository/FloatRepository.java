package com.globallogic.futbol.core.interfaces.repository;

/**
 * The common behaviour for all {@link Float} in the {@link android.content.SharedPreferences}
 *
 * @author facundo.mengoni
 * @since 0.3.4
 */
public interface FloatRepository extends SharedPreferenceRepository {
    Float getFloat();

    void putFloat(float value);
}