package com.globallogic.futbol.core.operation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import com.globallogic.futbol.core.LocalBroadcastManager;
import com.globallogic.futbol.core.OperationApp;
import com.globallogic.futbol.core.interfaces.IOperation;
import com.globallogic.futbol.core.interfaces.IOperationStrategy;
import com.globallogic.futbol.core.interfaces.IStrategyCallback;
import com.globallogic.futbol.core.operation.strategies.StrategyMockResponse;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Operation implements Serializable, IOperation, IStrategyCallback {
    //region Constants
    private static final String TAG = Operation.class.getSimpleName();
    private static final String SAVE_INSTANCE_TIME_INIT = "SAVE_INSTANCE_TIME_INIT";
    private static final String SAVE_INSTANCE_ID = "SAVE_INSTANCE_ID";
    private static final String SAVE_INSTANCE_STATE = "SAVE_INSTANCE_STATE";
    private static final String SAVE_INSTANCE_STRATEGY = "SAVE_INSTANCE_STRATEGY";
    //endregion

    //region Log
    public static Logger mLogger;

    static {
        mLogger = Logger.getLogger(TAG);
        mLogger.setLevel(Level.OFF);
    }
    //endregion

    //region Variables
    public Long mConnectionDelay = 0l;
    private Long timeInit;
    private String id;

    private OperationStatus mOperationStatus;
    private IOperationStrategy mStrategy;
    //endregion

    //region Constructors

    /**
     * Create a new instance with an id empty.
     * <p>
     * The id is used to register the receiver for a specific operation.
     * If you register two operation with different ids then the receiver
     * of one operation never listen the other operation.
     */
    protected Operation() {
        this("");
    }

    /**
     * Create a new instance with the specified id.
     * <p>
     * The id is used to register the receiver for a specific operation.
     * If you register two operation with different ids then the receiver
     * of one operation never listen the other operation.
     */
    protected Operation(String id) {
        this.id = id;
        reset();
    }
    //endregion

    //region Methods for test

    /**
     * Run the operation synchronously and return the response expected in the callback of the broadcast
     *
     * @see Operation#beforeWorkInBackground()
     * @see Operation#workInBackground(Exception, int, String)
     * @see Operation#afterWorkInBackground(Boolean)
     */
    public void testResponse(StrategyMockResponse aMockResponse) {
        beforeWorkInBackground();
        Boolean result = workInBackground(null, aMockResponse.getHttpCode(), aMockResponse.getResponse());
        afterWorkInBackground(result);
    }

    /**
     * Run the operation synchronously and return the exception expected in the callback of the broadcast
     *
     * @see Operation#beforeWorkInBackground()
     * @see Operation#workInBackground(Exception, int, String)
     * @see Operation#afterWorkInBackground(Boolean)
     */
    public void testResponse(Exception anException) {
        beforeWorkInBackground();
        Boolean result = workInBackground(anException, 0, null);
        afterWorkInBackground(result);
    }
    //endregion

    //region IStrategyCallback

    /**
     * Analysis of the response returned by the server
     *
     * @param aException the exception thrown because of some error
     * @param aHttpCode  the http response code
     * @param aString    the string of the response
     * @see Operation#workInBackground(Exception, int, String)
     * @see Operation#afterWorkInBackground(Boolean)
     */
    public void parseResponse(final Exception aException, final int aHttpCode, final String aString) {
        if (aException != null)
            mLogger.log(Level.SEVERE, aException.getMessage(), aException);
        if (TextUtils.isEmpty(aString) || !(aString.startsWith("{") || aString.startsWith("[")))
            mLogger.severe("Result: " + aString);
        else
            mLogger.info("Result: " + aString);

        // Parse and analyze
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                return workInBackground(aException, aHttpCode, aString);
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
     * It registers that the operation would be executed but for some reason
     * could not be implemented and that there is someone waiting to do.
     */
    @Override
    public IOperation setAsWaiting() {
        mOperationStatus = OperationStatus.WAITING_EXECUTION;
        return this;
    }

    /**
     * Executes the operation with the specified parameters.
     *
     * @param arg The arguments of the operation.
     * @see Operation#reset()
     * @see Operation#beforeWorkInBackground()
     * @see Operation#simulateWaiting(Object...)
     * @see Operation#doRequest(Object...)
     */
    @Override
    public boolean performOperation(Object... arg) {
        switch (mOperationStatus) {
            default:
            case UNKNOWN:
            case READY_TO_EXECUTE:
            case WAITING_EXECUTION:
                beforeWorkInBackground();
                if (mConnectionDelay > 0) {
                    simulateWaiting(arg);
                } else {
                    doRequest(arg);
                }
                return true;
            case FINISHED_EXECUTION:
                reset();
                return performOperation(arg);
            case DOING_EXECUTION:
                return false;
        }
    }

    //region Broadcast

    /**
     * It allows you to add extras to the intent that will be received by the receiver.
     * Is triggered only if you can connect to the server.
     */
    protected abstract void addExtrasForResultOk(Intent intent);

    /**
     * It allows you to add extras to the intent that will be received by the receiver.
     * Is triggered only if an error has occurred.
     */
    protected abstract void addExtrasForResultError(Intent intent);

    public void sendBroadcastForStart() {
        Intent intent = new Intent();
        intent.putExtra(OperationResult.EXTRA_STATUS, OperationResult.START.name);

        String actionWithId = OperationBroadcastReceiver.getActionForStart(this);
        intent.setAction(actionWithId);
        LocalBroadcastManager.getInstance(OperationApp.getInstance()).sendBroadcast(intent);

        String actionWithOutID = OperationBroadcastReceiver.getActionForStart(getClass());
        if (!actionWithId.equals(actionWithOutID)) {
            intent.setAction(actionWithOutID);
            LocalBroadcastManager.getInstance(OperationApp.getInstance()).sendBroadcast(intent);
        }
    }

    public void sendBroadcastForOk() {
        Intent intent = new Intent();
        intent.putExtra(OperationResult.EXTRA_STATUS, OperationResult.OK.name);
        addExtrasForResultOk(intent);

        String actionWithId = OperationBroadcastReceiver.getActionForOk(this);
        intent.setAction(actionWithId);
        LocalBroadcastManager.getInstance(OperationApp.getInstance()).sendBroadcast(intent);

        String actionWithOutID = OperationBroadcastReceiver.getActionForOk(getClass());
        if (!actionWithId.equals(actionWithOutID)) {
            intent.setAction(actionWithOutID);
            LocalBroadcastManager.getInstance(OperationApp.getInstance()).sendBroadcast(intent);
        }
    }

    public void sendBroadcastForError() {
        Intent intent = new Intent();
        intent.putExtra(OperationResult.EXTRA_STATUS, OperationResult.ERROR.name);
        addExtrasForResultError(intent);

        String actionWithId = OperationBroadcastReceiver.getActionForError(this);
        intent.setAction(actionWithId);
        LocalBroadcastManager.getInstance(OperationApp.getInstance()).sendBroadcast(intent);

        String actionWithOutID = OperationBroadcastReceiver.getActionForError(getClass());
        if (!actionWithId.equals(actionWithOutID)) {
            intent.setAction(actionWithOutID);
            LocalBroadcastManager.getInstance(OperationApp.getInstance()).sendBroadcast(intent);
        }
    }
    //endregion
    //endregion

    //region Lazy work

    /**
     * Returns to the original state of the operation
     */
    public void reset() {
        mOperationStatus = OperationStatus.READY_TO_EXECUTE;
    }

    /**
     * Execute the request with the strategy
     *
     * @see IOperationStrategy
     */
    private void doRequest(Object... arg) {
        mStrategy = getStrategy(arg);
        mStrategy.doRequest(this);
    }

    /**
     * Simulate a delay in the connection and then execute the request
     *
     * @see Operation#setConnectionDelay(int)
     * @see Operation#setConnectionDelay(long)
     * @see Operation#doRequest(Object...)
     */
    private void simulateWaiting(final Object... arg) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(new Random().nextInt(mConnectionDelay.intValue()));
                } catch (InterruptedException e) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                doRequest(arg);
            }
        }.execute((Void) null);
    }

    /**
     * Called when the operation is finished but before the receiver is notified.
     *
     * @param duration The duration of the request from which start until finish
     */
    protected void onOperationFinish(Long duration) {
    }
    //endregion

    //region Hard work

    /**
     * Returns a strategy for server connection
     *
     * @param arg The arguments specified in performOperation()
     */
    protected abstract IOperationStrategy getStrategy(Object... arg);

    /**
     * Initialize variables and send the broadcast to notify that the operation starts
     *
     * @see Operation#sendBroadcastForStart()
     */
    private void beforeWorkInBackground() {
        mOperationStatus = OperationStatus.DOING_EXECUTION;
        timeInit = Calendar.getInstance().getTimeInMillis();
        sendBroadcastForStart();
    }

    /**
     * Analyze the parameters to determine what would do
     *
     * @param anException The exception occurred
     * @param aHttpCode   The httpCode obtained
     * @param aString     The aString obtained
     * @see Operation#analyzeException(Exception)
     * @see Operation#analyzeResult(int, String)
     */
    private Boolean workInBackground(Exception anException, int aHttpCode, String aString) {
        if (anException != null) {
            analyzeException(anException);
            return false;
        } else {
            try {
                analyzeResult(aHttpCode, aString);
            } catch (Exception e2) {
                mLogger.log(Level.INFO, "Error in analyzeResult: " + e2.getMessage(), e2);
                analyzeException(e2);
                return false;
            }
            return true;
        }
    }

    /**
     * Update the variables and send the broadcast to notify that the operation finished
     *
     * @param aResult Boolean that notify the result of the operation
     * @see Operation#onOperationFinish(Long)
     * @see Operation#sendBroadcastForOk()
     * @see Operation#sendBroadcastForError()
     */
    private void afterWorkInBackground(Boolean aResult) {
        mOperationStatus = OperationStatus.FINISHED_EXECUTION;
        if (timeInit != null) {
            long timeFinish = Calendar.getInstance().getTimeInMillis();
            long difference = timeFinish - timeInit;
            onOperationFinish(difference);
        }
        if (aResult) {
            sendBroadcastForOk();
        } else {
            sendBroadcastForError();
        }
    }
    //endregion

    //region Android lifecycle
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null)
            //Restauro los datos necesario de la operacion
            onRestoreInstanceState(savedInstanceState);
    }

    public void onSaveInstanceState(Bundle outState) {
        if (timeInit != null)
            outState.putLong(SAVE_INSTANCE_TIME_INIT, timeInit);
        outState.putString(SAVE_INSTANCE_ID, id);

        outState.putSerializable(SAVE_INSTANCE_STATE, mOperationStatus);
        if (mStrategy != null)
            outState.putSerializable(SAVE_INSTANCE_STRATEGY, mStrategy);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        id = savedInstanceState.getString(SAVE_INSTANCE_ID);
        mOperationStatus = (OperationStatus) savedInstanceState.getSerializable(SAVE_INSTANCE_STATE);
        if (savedInstanceState.containsKey(SAVE_INSTANCE_STRATEGY)) {
            mStrategy = (IOperationStrategy) savedInstanceState.getSerializable(SAVE_INSTANCE_STRATEGY);
            mStrategy.updateCallback(this);
        }
        if (savedInstanceState.containsKey(SAVE_INSTANCE_TIME_INIT))
            timeInit = savedInstanceState.getLong(SAVE_INSTANCE_TIME_INIT);
    }
    //endregion

    //region Getters & Setters

    /**
     * @return The status of the operation
     * @see OperationStatus
     */
    public OperationStatus getStatus() {
        return mOperationStatus;
    }

    /**
     * Defines a time delay for the operation
     *
     * @see Operation#simulateWaiting(Object...)
     */
    protected void setConnectionDelay(int duration) {
        this.mConnectionDelay = (long) duration;
    }

    /**
     * Defines a time delay for the operation
     *
     * @see Operation#simulateWaiting(Object...)
     */
    protected void setConnectionDelay(long duration) {
        this.mConnectionDelay = duration;
    }

    /**
     * @return The id of the operation
     */
    public String getId() {
        return id;
    }

    /**
     * Defines an id for the operation
     *
     * @see Operation#Operation(String)
     */
    public void setId(String id) {
        if (id == null)
            id = "";
        this.id = id;
    }
    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Operation operation = (Operation) o;

        if (!TextUtils.isEmpty(id) ? !id.equals(operation.id) : !TextUtils.isEmpty(operation.id))
            return false;
        if (timeInit != null ? !timeInit.equals(operation.timeInit) : operation.timeInit != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = timeInit != null ? timeInit.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}