package com.globallogic.futbol.core.strategies;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import com.globallogic.futbol.core.exceptions.KeyNotFound;
import com.globallogic.futbol.core.exceptions.UnexpectedResponseException;
import com.globallogic.futbol.core.interfaces.analyzers.IStrategySharedPreferenceAnalyzer;
import com.globallogic.futbol.core.interfaces.parsers.IOperationParser;
import com.globallogic.futbol.core.interfaces.repository.FloatRepository;
import com.globallogic.futbol.core.interfaces.repository.IntegerRepository;
import com.globallogic.futbol.core.interfaces.repository.LongRepository;
import com.globallogic.futbol.core.interfaces.repository.SharedPreferenceRepository;
import com.globallogic.futbol.core.interfaces.repository.StringRepository;
import com.globallogic.futbol.core.interfaces.repository.StringSetRepository;
import com.globallogic.futbol.core.operations.Operation;
import com.globallogic.futbol.core.responses.StrategySharedPreferenceResponse;

import java.util.logging.Level;

/**
 * A specific implementation to helps to manage data from shared preferences.
 *
 * @author facundo.mengoni
 * @see OperationStrategy
 * @see IOperationParser
 * @see StrategySharedPreferenceResponse
 * @see IStrategySharedPreferenceAnalyzer
 * @see FloatRepository
 * @see IntegerRepository
 * @see LongRepository
 * @see StringRepository
 * @see StringSetRepository
 * @since 0.3.4
 */
public abstract class SharedPreferenceStrategy<T, U extends SharedPreferenceRepository> extends OperationStrategy<StrategySharedPreferenceResponse<T>> implements IOperationParser<StrategySharedPreferenceResponse<T>> {
    protected final U mRepository;
    private boolean wasCanceled;

    //region Constructors implementation
    public SharedPreferenceStrategy(Operation anOperation, IStrategySharedPreferenceAnalyzer anAnalyzer, U repository) {
        super(anOperation, anAnalyzer);
        this.mRepository = repository;
    }
    //endregion

    @Override
    public void cancel() {
        wasCanceled = true;
    }

    public U getRepository() {
        return mRepository;
    }

    //region OperationStrategy implementation
    /**
     * {@inheritDoc}
     */
    @Override
    public IStrategySharedPreferenceAnalyzer getAnalyzer() {
        return (IStrategySharedPreferenceAnalyzer) super.getAnalyzer();
    }
    //endregion

    //region IOperationHttpParser implementation
    /**
     * {@inheritDoc}
     */
    @Override
    public void parseResponse(final Exception anException, final StrategySharedPreferenceResponse<T> aStrategyResponse) {
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            T object = null;

            @Override
            protected void onPreExecute() {
                if (aStrategyResponse != null) {
                    object = aStrategyResponse.getResult();
                }
                if (anException != null)
                    mLogger.log(Level.SEVERE, String.format("Parsing response: %s", anException.getMessage()), anException);
                mLogger.info(String.format("Parsing response: %s", object));
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                return workInBackground(anException, object);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                afterWorkInBackground(result);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ((Void) null));
        } else {
            task.execute();
        }
    }

    public Boolean workInBackground(Exception anException, T object) {
        if (wasCanceled) {
            return false;
        }
        mLogger.info("Work in background");
        if (anException != null) {
            getAnalyzer().analyzeException(anException);
            return false;
        } else {
            try {
                if (!getAnalyzer().analyzeResult(object))
                    throw new UnexpectedResponseException();
            } catch (Exception e2) {
                mLogger.log(Level.INFO, "Error in analyzeResult: " + e2.getMessage(), e2);
                getAnalyzer().analyzeException(e2);
                return false;
            }
            return true;
        }
    }
    //endregion
}