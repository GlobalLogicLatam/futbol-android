package com.globallogic.futbol.core.strategies.sharedpreference;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategySharedPreferenceAnalyzer;
import com.globallogic.futbol.core.interfaces.repository.IntegerRepository;
import com.globallogic.futbol.core.operations.Operation;

/**
 * A specific implementation to helps to obtains data from shared preferences.
 *
 * @author facundo.mengoni
 * @see GetSharedPreferenceStrategy
 * @since 0.3.6
 */
public abstract class PutIntegerSharedPreferenceStrategy extends PutSharedPreferenceStrategy<Integer, IntegerRepository> {
    public PutIntegerSharedPreferenceStrategy(Operation anOperation, IStrategySharedPreferenceAnalyzer anAnalyzer, IntegerRepository repository) {
        super(anOperation, anAnalyzer, repository);
    }

    @Override
    protected void saveData(IntegerRepository repository) {
        repository.putInteger(dataToSave(repository));
    }
}