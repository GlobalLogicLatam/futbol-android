package com.globallogic.futbol.core.strategies;

import android.os.AsyncTask;
import android.os.Build;

import com.globallogic.futbol.core.exceptions.UnexpectedResponseException;
import com.globallogic.futbol.core.interfaces.analyzers.IStrategyDbAnalyzer;
import com.globallogic.futbol.core.interfaces.parsers.IOperationDbParser;
import com.globallogic.futbol.core.operations.Operation;
import com.globallogic.futbol.core.responses.StrategyDbResponse;

import java.util.logging.Level;

/**
 * A specific implementation to helps to do a db request.
 *
 * @author facundo.mengoni
 * @see OperationStrategy
 * @see IOperationDbParser
 * @see StrategyDbResponse
 * @see IStrategyDbAnalyzer
 * @since 0.1.0
 */
public abstract class DbOperationStrategy<T> extends OperationStrategy<StrategyDbResponse<T>> implements IOperationDbParser<T, StrategyDbResponse<T>> {
    //region Constructors implementation
    public DbOperationStrategy(Operation anOperation, IStrategyDbAnalyzer<T> anAnalyzer) {
        super(anOperation, anAnalyzer);
    }
    //endregion

    //region OperationStrategy implementation

    /**
     * {@inheritDoc}
     */
    @Override
    public IStrategyDbAnalyzer<T> getAnalyzer() {
        //noinspection unchecked
        return (IStrategyDbAnalyzer<T>) super.getAnalyzer();
    }
    //endregion

    //region IOperationDbParser implementation

    /**
     * {@inheritDoc}
     */
    @Override
    public void parseResponse(final Exception anException, final StrategyDbResponse<T> aStrategyResponse) {
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            T aResponse = null;

            @Override
            protected void onPreExecute() {
                if (aStrategyResponse != null) {
                    aResponse = aStrategyResponse.getResponse();
                }
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                return workInBackground(anException, aResponse);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean workInBackground(Exception anException, T aResponse) {
        mLogger.info("Work in background");
        if (anException != null) {
            getAnalyzer().analyzeException(anException);
            return false;
        } else {
            try {
                if (!getAnalyzer().analyzeResult(aResponse))
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