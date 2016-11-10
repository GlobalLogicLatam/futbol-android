package com.globallogic.futbol.core.strategies.sharedpreference;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategySharedPreferenceAnalyzer;
import com.globallogic.futbol.core.interfaces.repository.BooleanRepository;
import com.globallogic.futbol.core.operations.Operation;

/**
 * A specific implementation to helps to obtains data from shared preferences.
 *
 * @author facundo.mengoni
 * @see GetSharedPreferenceStrategy
 * @since 0.3.7
 */
public abstract class PutBooleanSharedPreferenceStrategy extends PutSharedPreferenceStrategy<Boolean, BooleanRepository> {
    public PutBooleanSharedPreferenceStrategy(Operation anOperation, IStrategySharedPreferenceAnalyzer anAnalyzer, BooleanRepository repository) {
        super(anOperation, anAnalyzer, repository);
    }

    @Override
    protected void saveData(BooleanRepository repository) {
        repository.putBoolean(dataToSave(repository));
    }
}