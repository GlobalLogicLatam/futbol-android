package com.globallogic.futbol.core.operation;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.globallogic.futbol.core.OperationResponse;
import com.globallogic.futbol.core.exceptions.UnexpectedResponseException;
import com.globallogic.futbol.core.operation.strategies.StrategyHttpMockResponse;

import java.util.logging.Level;

public abstract class OperationHttp extends Operation<Integer, String> {

    //region Constructors

    /**
     * Create a new instance with an id empty.
     * <p/>
     * The id is used to register the receiver for a specific operation.
     * If you register two operation with different ids then the receiver
     * of one operation never listen the other operation.
     */
    protected OperationHttp() {
        this("");
    }

    /**
     * Create a new instance with the specified id.
     * <p/>
     * The id is used to register the receiver for a specific operation.
     * If you register two operation with different ids then the receiver
     * of one operation never listen the other operation.
     */
    protected OperationHttp(String id) {
        mLogger.info(String.format("Constructor with id: %s", id));
        this.id = id;
        reset();
    }
    //endregion

    //region Methods for test

    /**
     * Run the operation synchronously and return the response expected in the callback of the broadcast
     *
     * @see OperationHttp#beforeWorkInBackground()
     * @see OperationHttp#workInBackground(Exception, int, String)
     * @see OperationHttp#afterWorkInBackground(Boolean)
     */
    public Boolean testResponse(StrategyHttpMockResponse aMockResponse) {
        mLogger.info(String.format("Test response: %s", aMockResponse.toString()));
        switch (mOperationStatus) {
            default:
            case UNKNOWN:
            case READY_TO_EXECUTE:
            case WAITING_EXECUTION:
                if (!hasInternet()) {
                    sendBroadcastForNoInternet();
                    return false;
                }
                beforeWorkInBackground();
                Boolean result = workInBackground(null, aMockResponse.getHttpCode(), aMockResponse.getResponse());
                afterWorkInBackground(result);
                return true;
            case FINISHED_EXECUTION:
                afterWorkInBackgroundBroadcasts(mResult);
                return false;
            case DOING_EXECUTION:
                beforeWorkInBackgroundBroadcasts();
                return false;
        }
    }

    //endregion

    //region IStrategyCallback
    /**
     * Analysis of the response returned by the server
     *
     * @param aException the exception thrown because of some error
     * @param aResponse the http response
     * @see OperationHttp#workInBackground(Exception, int, String)
     * @see OperationHttp#afterWorkInBackground(Boolean)
     */

    @Override
    public void parseResponse(final Exception aException, final OperationResponse<Integer, String> aResponse) {
        if (aException != null)
            mLogger.log(Level.SEVERE, String.format("Parsing response: %s", aException.getMessage()), aException);
        if (TextUtils.isEmpty(aResponse.getResult()) || !(aResponse.getResult().startsWith("{") || aResponse.getResult().startsWith("[")))
            mLogger.severe(String.format("Parsing response: %s", aResponse.getResult()));
        else
            mLogger.info(String.format("Parsing response: %s", aResponse.getResult()));

        // Parse and analyze
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                return workInBackground(aException, aResponse.getResultCode(), aResponse.getResult());
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                afterWorkInBackground(result);
            }
        }.execute((Void) null);
    }
    //endregion

    //region IOperation

    /**
     * Analyze the parameters to determine what would do
     *
     * @param anException The exception occurred
     * @param aHttpCode   The httpCode obtained
     * @param aString     The aString obtained
     * @see OperationHttp#analyzeException(Exception)
     */
    protected Boolean workInBackground(Exception anException, int aHttpCode, String aString) {
        mLogger.info("Work in background");
        if (anException != null) {
            analyzeException(anException);
            return false;
        } else {
            try {
                if (!analyzeResult(new HttpOperationResponse(aHttpCode, aString)))
                    throw new UnexpectedResponseException();
            } catch (Exception e2) {
                mLogger.log(Level.INFO, "Error in analyzeResult: " + e2.getMessage(), e2);
                analyzeException(e2);
                return false;
            }
            return true;
        }
    }

    //endregion

}