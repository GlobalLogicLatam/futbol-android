package com.globallogic.futbol.core.strategies.sharedpreference;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategySharedPreferenceAnalyzer;
import com.globallogic.futbol.core.interfaces.repository.LongRepository;
import com.globallogic.futbol.core.operations.Operation;

/**
 * A specific implementation to helps to obtains data from shared preferences.
 *
 * @author facundo.mengoni
 * @see GetSharedPreferenceStrategy
 * @since 0.3.6
 */
public class GetLongSharedPreferenceStrategy extends GetSharedPreferenceStrategy<Long, LongRepository> {
    public GetLongSharedPreferenceStrategy(Operation anOperation, IStrategySharedPreferenceAnalyzer anAnalyzer, LongRepository repository) {
        super(anOperation, anAnalyzer, repository);
    }

    @Override
    protected Long getResult(LongRepository repository) {
        return repository.getLong();
    }
}