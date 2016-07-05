package com.globallogic.futbol.core.strategies.sharedpreference;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategySharedPreferenceAnalyzer;
import com.globallogic.futbol.core.interfaces.repository.StringRepository;
import com.globallogic.futbol.core.operations.Operation;

/**
 * A specific implementation to helps to obtains data from shared preferences.
 *
 * @author facundo.mengoni
 * @see GetSharedPreferenceStrategy
 * @since 0.3.6
 */
public abstract class PutStringSharedPreferenceStrategy extends PutSharedPreferenceStrategy<String, StringRepository> {
    public PutStringSharedPreferenceStrategy(Operation anOperation, IStrategySharedPreferenceAnalyzer anAnalyzer, StringRepository repository) {
        super(anOperation, anAnalyzer, repository);
    }

    @Override
    protected void saveData(StringRepository repository) {
        repository.putString(dataToSave(repository));
    }
}