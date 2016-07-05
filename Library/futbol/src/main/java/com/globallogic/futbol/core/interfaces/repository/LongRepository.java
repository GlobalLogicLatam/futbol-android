package com.globallogic.futbol.core.interfaces.repository;

/**
 * The common behaviour for all {@link Long} in the {@link android.content.SharedPreferences}
 *
 * @author facundo.mengoni
 * @since 0.3.4
 */
public interface LongRepository extends SharedPreferenceRepository {
    Long getLong();

    void putLong(long value);
}