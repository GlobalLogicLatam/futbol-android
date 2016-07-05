package com.globallogic.futbol.core.strategies.sharedpreference;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategySharedPreferenceAnalyzer;
import com.globallogic.futbol.core.interfaces.repository.StringSetRepository;
import com.globallogic.futbol.core.operations.Operation;

import java.util.Set;

/**
 * A specific implementation to helps to obtains data from shared preferences.
 *
 * @author facundo.mengoni
 * @see GetSharedPreferenceStrategy
 * @since 0.3.6
 */
public abstract class PutStringSetSharedPreferenceStrategy extends PutSharedPreferenceStrategy<Set<String>, StringSetRepository> {
    public PutStringSetSharedPreferenceStrategy(Operation anOperation, IStrategySharedPreferenceAnalyzer anAnalyzer, StringSetRepository repository) {
        super(anOperation, anAnalyzer, repository);
    }

    @Override
    protected void saveData(StringSetRepository repository) {
        repository.putStringSet(dataToSave(repository));
    }
}