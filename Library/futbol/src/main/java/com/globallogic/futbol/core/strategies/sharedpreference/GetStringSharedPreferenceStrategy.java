package com.globallogic.futbol.core.strategies.sharedpreference;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategySharedPreferenceAnalyzer;
import com.globallogic.futbol.core.interfaces.repository.StringRepository;
import com.globallogic.futbol.core.operations.Operation;
import com.globallogic.futbol.core.strategies.SharedPreferenceStrategy;

/**
 * A specific implementation to helps to obtains data from shared preferences.
 *
 * @author facundo.mengoni
 * @see GetSharedPreferenceStrategy
 * @since 0.3.6
 */
public class GetStringSharedPreferenceStrategy extends GetSharedPreferenceStrategy<String, StringRepository> {
    public GetStringSharedPreferenceStrategy(Operation anOperation, IStrategySharedPreferenceAnalyzer anAnalyzer, StringRepository repository) {
        super(anOperation, anAnalyzer, repository);
    }

    @Override
    protected String getResult(StringRepository repository) {
        return repository.getString();
    }
}