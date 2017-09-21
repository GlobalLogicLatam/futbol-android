package com.globallogic.futbol.core.strategies.sharedpreference;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategySharedPreferenceAnalyzer;
import com.globallogic.futbol.core.interfaces.repository.SharedPreferenceRepository;
import com.globallogic.futbol.core.operations.Operation;
import com.globallogic.futbol.core.responses.StrategySharedPreferenceResponse;
import com.globallogic.futbol.core.strategies.SharedPreferenceStrategy;

/**
 * A specific implementation to helps to clean a key from shared preferences.
 *
 * @author facundo.mengoni
 * @since 0.3.8
 */
public class RemoveSharedPreferenceStrategy extends SharedPreferenceStrategy<Void, SharedPreferenceRepository> {
    public RemoveSharedPreferenceStrategy(Operation anOperation, IStrategySharedPreferenceAnalyzer anAnalyzer, SharedPreferenceRepository repository) {
        super(anOperation, anAnalyzer, repository);
    }

    @Override
    protected void doRequestImpl() {
        getRepository().removeKey();
        parseResponse(null, new StrategySharedPreferenceResponse<Void>());
    }
}