package com.globallogic.futbol.core.strategies.sharedpreference;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategySharedPreferenceAnalyzer;
import com.globallogic.futbol.core.interfaces.repository.FloatRepository;
import com.globallogic.futbol.core.operations.Operation;

/**
 * A specific implementation to helps to obtains data from shared preferences.
 *
 * @author facundo.mengoni
 * @see GetSharedPreferenceStrategy
 * @since 0.3.6
 */
public abstract class PutFloatSharedPreferenceStrategy extends PutSharedPreferenceStrategy<Float, FloatRepository> {
    public PutFloatSharedPreferenceStrategy(Operation anOperation, IStrategySharedPreferenceAnalyzer anAnalyzer, FloatRepository repository) {
        super(anOperation, anAnalyzer, repository);
    }

    @Override
    protected void saveData(FloatRepository repository) {
        repository.putFloat(dataToSave(repository));
    }
}