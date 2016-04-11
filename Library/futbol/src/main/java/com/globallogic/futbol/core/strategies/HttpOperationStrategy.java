package com.globallogic.futbol.core.strategies;

import android.content.Intent;
import android.text.TextUtils;

import com.globallogic.futbol.core.LocalBroadcastManager;
import com.globallogic.futbol.core.OperationApp;
import com.globallogic.futbol.core.OperationResult;
import com.globallogic.futbol.core.broadcasts.OperationBroadcastReceiverHelper;
import com.globallogic.futbol.core.exceptions.UnexpectedResponseException;
import com.globallogic.futbol.core.interfaces.analyzers.IStrategyHttpAnalyzer;
import com.globallogic.futbol.core.interfaces.parsers.IOperationHttpParser;
import com.globallogic.futbol.core.operations.Operation;
import com.globallogic.futbol.core.responses.StrategyHttpResponse;
import com.globallogic.futbol.core.utils.Utils;

import java.util.Calendar;
import java.util.logging.Level;

/**
 * A specific implementation to helps to do a http request.
 *
 * @author facundo.mengoni
 * @see OperationStrategy
 * @see IOperationHttpParser
 * @see StrategyHttpResponse
 * @see IStrategyHttpAnalyzer
 * @since 0.1.0
 */
public abstract class HttpOperationStrategy extends OperationStrategy<StrategyHttpResponse> implements IOperationHttpParser<StrategyHttpResponse> {
    //region Constructors implementation
    public HttpOperationStrategy(Operation anOperation, IStrategyHttpAnalyzer anAnalyzer) {
        super(anOperation, anAnalyzer);
    }
    //endregion

    //region OperationStrategy implementation

    /**
     * {@inheritDoc}
     */
    @Override
    public IStrategyHttpAnalyzer getAnalyzer() {
        return (IStrategyHttpAnalyzer) super.getAnalyzer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        if (!Utils.hasInternet(OperationApp.getInstance())) {
            sendBroadcastForNoInternet();
            mOperation.onStrategyFinish(this);
        } else {
            super.execute();
        }
    }
    //endregion

    //region HttpOperationStrategy implementation

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

    private void sendBroadcastForNoInternet() {
        mLogger.info("Sending broadcast for no internet");
        Intent intent = new Intent();
        intent.putExtra(OperationResult.EXTRA_OPERATION_RESULT, OperationResult.NO_INTERNET.name);

        String actionWithId = OperationBroadcastReceiverHelper.getActionForNoInternet(mOperation);
        intent.setAction(actionWithId);
        LocalBroadcastManager.getInstance(OperationApp.getInstance()).sendBroadcast(intent);

        String actionWithOutID = OperationBroadcastReceiverHelper.getActionForNoInternet(getClass());
        if (!actionWithId.equals(actionWithOutID)) {
            intent.setAction(actionWithOutID);
            LocalBroadcastManager.getInstance(OperationApp.getInstance()).sendBroadcast(intent);
        }
    }
    //endregion

    //region IOperationHttpParser implementation

    /**
     * {@inheritDoc}
     */
    @Override
    public void parseResponse(Exception anException, StrategyHttpResponse aStrategyResponse) {
        Utils.thisMethodShouldNotExecuteInTheThreadUI();
        String aString = "";
        Integer aHttpCode = 0;
        if (aStrategyResponse != null) {
            aString = aStrategyResponse.getResponse();
            aHttpCode = aStrategyResponse.getHttpCode();
        }
        if (anException != null)
            mLogger.log(Level.SEVERE, String.format("Parsing response: %s", anException.getMessage()), anException);
        if (TextUtils.isEmpty(aString) || !(aString.startsWith("{") || aString.startsWith("[")))
            mLogger.severe(String.format("Parsing response: %s", aString));
        else
            mLogger.info(String.format("Parsing response: %s", aString));

        boolean result = workInBackground(anException, aHttpCode, aString);
        afterWorkInBackground(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean workInBackground(Exception anException, Integer aHttpCode, String aString) {
        mLogger.info("Work in background");
        if (anException != null) {
            getAnalyzer().analyzeException(anException);
            return false;
        } else {
            try {
                if (!getAnalyzer().analyzeResult(aHttpCode, aString))
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