package com.globallogic.futbol.core.operation;

import android.database.Cursor;
import android.os.AsyncTask;

import com.globallogic.futbol.core.OperationApp;
import com.globallogic.futbol.core.OperationResponse;
import com.globallogic.futbol.core.exceptions.UnexpectedResponseException;
import com.globallogic.futbol.core.operation.database.OperationDatabaseHelper;
import com.globallogic.futbol.core.operation.strategies.SqliteOperationResponse;
import com.globallogic.futbol.core.operation.strategies.StrategySqliteMockResponse;

import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by Agustin Larghi on 07/10/2015.
 * Globallogic
 * agustin.larghi@globallogic.com
 */
public abstract class OperationSqlite extends Operation<String, Cursor> {

    //region Constructors

    /**
     * Create a new instance with an id empty.
     * <p/>
     * The id is used to register the receiver for a specific operation.
     * If you register two operation with different ids then the receiver
     * of one operation never listen the other operation.
     */
    protected OperationSqlite() {
        this("");
    }

    /**
     * Create a new instance with the specified id.
     * <p/>
     * The id is used to register the receiver for a specific operation.
     * If you register two operation with different ids then the receiver
     * of one operation never listen the other operation.
     */
    protected OperationSqlite(String id) {
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
    protected Boolean testResponse(StrategySqliteMockResponse aMockResponse) {
        mLogger.info(String.format("Test response: %s", aMockResponse.toString()));
        switch (mOperationStatus) {
            default:
            case UNKNOWN:
            case READY_TO_EXECUTE:
            case WAITING_EXECUTION:
                beforeWorkInBackground();
                Boolean result = false;
                try {
                    OperationDatabaseHelper mHelper = new OperationDatabaseHelper(OperationApp.getInstance(), aMockResponse.getAssetsDatabasePath());
                    mHelper.createDataBase();
                    mHelper.openDataBase();
                    Cursor resultCursor = mHelper.getReadableDatabase().rawQuery(aMockResponse.getQuery(), null);
                    mHelper.close();
                    result = workInBackground(null, aMockResponse.getResultCode(), resultCursor);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
     * @param aResponse  the http response
     * @see OperationHttp#workInBackground(Exception, int, String)
     * @see OperationHttp#afterWorkInBackground(Boolean)
     */

    @Override
    public void parseResponse(final Exception aException, final OperationResponse<String, Cursor> aResponse) {
        if (aException != null)
            mLogger.log(Level.SEVERE, String.format("Parsing response: %s", aException.getMessage()), aException);
        // Parse and analyze
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                return workInBackground(aException, 0, aResponse.getResult());
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
     * @param aStatusCode The code obtained
     * @see OperationHttp#analyzeException(Exception)
     */
    protected Boolean workInBackground(Exception anException, int aStatusCode, Cursor aResponse) {
        mLogger.info("Work in background");
        if (anException != null) {
            analyzeException(anException);
            return false;
        } else {
            try {
                if (!analyzeResult(new SqliteOperationResponse("", aResponse)))
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
