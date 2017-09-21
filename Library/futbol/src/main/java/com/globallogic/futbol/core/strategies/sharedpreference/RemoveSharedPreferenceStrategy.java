package com.globallogic.futbol.core.strategies.sharedpreference;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategySharedPreferenceAnalyzer;
import com.globallogic.futbol.core.interfaces.repository.BooleanRepository;
import com.globallogic.futbol.core.operations.Operation;

/**
 * A specific implementation to helps to clean a key from shared preferences.
 *
 * @author facundo.mengoni
 * @since 0.3.8
 */
public class RemoveSharedPreferenceStrategy extends GetSharedPreferenceStrategy<Boolean, BooleanRepository> {
    public RemoveSharedPreferenceStrategy(Operation anOperation, IStrategySharedPreferenceAnalyzer anAnalyzer, BooleanRepository repository) {
        super(anOperation, anAnalyzer, repository);
    }

    @Override
    protected Boolean getResult(BooleanRepository repository) {
        repository.removeKey();
        return true;
    }
}