package com.globallogic.futbol.core.strategies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;

import com.globallogic.futbol.core.OperationApp;
import com.globallogic.futbol.core.OperationResult;
import com.globallogic.futbol.core.broadcasts.OperationBroadcastReceiverHelper;
import com.globallogic.futbol.core.exceptions.UnexpectedResponseException;
import com.globallogic.futbol.core.interfaces.analyzers.IStrategyHttpAnalyzer;
import com.globallogic.futbol.core.interfaces.parsers.IOperationHttpParser;
import com.globallogic.futbol.core.operations.Operation;
import com.globallogic.futbol.core.responses.StrategyHttpResponse;
import com.globallogic.futbol.core.utils.Utils;

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
    private void sendBroadcastForNoInternet() {
        Intent intent = new Intent();
        intent.putExtra(OperationResult.EXTRA_OPERATION_RESULT, OperationResult.NO_INTERNET.name);

        String actionWithId = OperationBroadcastReceiverHelper.getActionForNoInternet(mOperation);
        intent.setAction(actionWithId);
        mOperation.sendBroadcast(intent);

        String actionWithOutID = OperationBroadcastReceiverHelper.getActionForNoInternet(getClass());
        if (!actionWithId.equals(actionWithOutID)) {
            intent.setAction(actionWithOutID);
            mOperation.sendBroadcast(intent);
        }
    }
    //endregion

    //region IOperationHttpParser implementation

    /**
     * {@inheritDoc}
     */
    @Override
    public void parseResponse(final Exception anException, final StrategyHttpResponse aStrategyResponse) {
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            String aString = "";
            Integer aHttpCode = 0;

            @Override
            protected void onPreExecute() {
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
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                return workInBackground(anException, aHttpCode, aString);
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
    public Boolean workInBackground(Exception anException, Integer aHttpCode, String aString) {
        getAnalyzer().reset();
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
    //endregion
}