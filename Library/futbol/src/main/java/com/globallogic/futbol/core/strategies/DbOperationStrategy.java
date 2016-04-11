package com.globallogic.futbol.core.strategies;

import com.globallogic.futbol.core.exceptions.UnexpectedResponseException;
import com.globallogic.futbol.core.interfaces.analyzers.IStrategyDbAnalyzer;
import com.globallogic.futbol.core.interfaces.parsers.IOperationDbParser;
import com.globallogic.futbol.core.operations.Operation;
import com.globallogic.futbol.core.responses.StrategyDbResponse;
import com.globallogic.futbol.core.utils.Utils;

import java.util.Calendar;
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

    //region DbOperationStrategy implementation

    /**
     * Send the broadcast to notify that the operation finished
     *
     * @param aResult Boolean that notify the result of the operation
     */
    private void afterWorkInBackgroundBroadcasts(Boolean aResult) {
        if (aResult) {
            sendBroadcastForOk();
        } else {
            sendBroadcastForError();
        }
    }
    //endregion

    //region IOperationDbParser implementation

    /**
     * {@inheritDoc}
     */
    @Override
    public void parseResponse(Exception anException, StrategyDbResponse<T> aStrategyResponse) {
        Utils.thisMethodShouldNotExecuteInTheThreadUI();
        T aResponse = null;
        if (aStrategyResponse != null) {
            aResponse = aStrategyResponse.getResponse();
        }
        boolean result = workInBackground(anException, aResponse);
        afterWorkInBackground(result);
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

    /**
     * {@inheritDoc}
     */
    public void afterWorkInBackground(Boolean aResult) {
        mLogger.info("After work in background");
        if (timeInit != null) {
            long timeFinish = Calendar.getInstance().getTimeInMillis();
            long difference = timeFinish - timeInit;
            onStrategyFinish(difference);
        }
        afterWorkInBackgroundBroadcasts(aResult);
    }
    //endregion
}