package com.globallogic.futbol.core.interfaces.repository;

import java.util.Set;

/**
 * The common behaviour for all {@link Set<String>} in the {@link android.content.SharedPreferences}
 *
 * @author facundo.mengoni
 * @since 0.3.4
 */
public interface StringSetRepository {
    Boolean hasKey();

    Set<String> getStringSet();

    void putStringSet(Set<String> value);
}
