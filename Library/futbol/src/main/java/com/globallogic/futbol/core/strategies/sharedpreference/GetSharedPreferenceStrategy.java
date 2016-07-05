package com.globallogic.futbol.core.strategies.sharedpreference;

import com.globallogic.futbol.core.exceptions.KeyNotFound;
import com.globallogic.futbol.core.interfaces.analyzers.IStrategySharedPreferenceAnalyzer;
import com.globallogic.futbol.core.interfaces.repository.SharedPreferenceRepository;
import com.globallogic.futbol.core.operations.Operation;
import com.globallogic.futbol.core.responses.StrategySharedPreferenceResponse;
import com.globallogic.futbol.core.strategies.SharedPreferenceStrategy;

/**
 * A specific implementation to helps to obtains data from shared preferences.
 *
 * @author facundo.mengoni
 * @see SharedPreferenceStrategy
 * @since 0.3.6
 */
public abstract class GetSharedPreferenceStrategy<T, U extends SharedPreferenceRepository> extends SharedPreferenceStrategy<T, U> {
    public GetSharedPreferenceStrategy(Operation anOperation, IStrategySharedPreferenceAnalyzer anAnalyzer, U repository) {
        super(anOperation, anAnalyzer, repository);
    }

    @Override
    protected void doRequestImpl() {
        if (getRepository().hasKey()) {
            StrategySharedPreferenceResponse<T> response = new StrategySharedPreferenceResponse<>();
            response.setResult(getResult(getRepository()));
            parseResponse(null, response);
        } else {
            parseResponse(new KeyNotFound(""), null);
        }
    }

    protected abstract T getResult(U repository);
}