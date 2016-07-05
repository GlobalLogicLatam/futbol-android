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
public class GetFloatSharedPreferenceStrategy extends GetSharedPreferenceStrategy<Float, FloatRepository> {
    public GetFloatSharedPreferenceStrategy(Operation anOperation, IStrategySharedPreferenceAnalyzer anAnalyzer, FloatRepository repository) {
        super(anOperation, anAnalyzer, repository);
    }

    @Override
    protected Float getResult(FloatRepository repository) {
        return repository.getFloat();
    }
}