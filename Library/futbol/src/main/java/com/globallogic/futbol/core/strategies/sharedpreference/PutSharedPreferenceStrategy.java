package com.globallogic.futbol.core.strategies.sharedpreference;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategySharedPreferenceAnalyzer;
import com.globallogic.futbol.core.interfaces.repository.SharedPreferenceRepository;
import com.globallogic.futbol.core.operations.Operation;
import com.globallogic.futbol.core.responses.StrategySharedPreferenceResponse;
import com.globallogic.futbol.core.strategies.SharedPreferenceStrategy;

/**
 * A specific implementation to helps to save data from shared preferences.
 *
 * @author facundo.mengoni
 * @see SharedPreferenceStrategy
 * @since 0.3.6
 */
public abstract class PutSharedPreferenceStrategy<T, U extends SharedPreferenceRepository> extends SharedPreferenceStrategy<T, U> {
    public PutSharedPreferenceStrategy(Operation anOperation, IStrategySharedPreferenceAnalyzer anAnalyzer, U repository) {
        super(anOperation, anAnalyzer, repository);
    }

    @Override
    protected void doRequestImpl() {
        saveData(getRepository());
        StrategySharedPreferenceResponse<T> response = new StrategySharedPreferenceResponse<>();
        response.setResult(dataToSave(getRepository()));
        parseResponse(null, response);
    }

    protected abstract void saveData(U repository);

    protected abstract T dataToSave(U repository);
}