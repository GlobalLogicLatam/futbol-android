package com.globallogic.futbol.core.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.globallogic.futbol.core.LocalBroadcastManager;
import com.globallogic.futbol.core.OperationApp;
import com.globallogic.futbol.core.OperationResult;
import com.globallogic.futbol.core.interfaces.callbacks.IOperationCallback;
import com.globallogic.futbol.core.operations.Operation;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A default class to implement your own receivers with the default cases (start, ok, error and finish) implemented.
 *
 * @author facundo.mengoni
 * @see IOperationCallback
 * @since 0.1.0
 */
public abstract class OperationBroadcastReceiver extends BroadcastReceiver {
    //region Logger
    public Logger mLogger;

    {
        mLogger = Logger.getLogger(getClass().getSimpleName());
        mLogger.setLevel(Level.OFF);
    }

    //endregion

    //region Variables
    private final IOperationCallback mCallback;
    //endregion

    //region Constructors implementation
    protected OperationBroadcastReceiver(IOperationCallback callback) {
        this.mCallback = callback;
    }
    //endregion

    //region BroadcastReceiver implementation
    @Override
    public void onReceive(Context aContext, Intent intent) {
        String status = intent.getStringExtra(OperationResult.EXTRA_OPERATION_RESULT);
        mLogger.info("onReceive: ".concat(status));
        checkStatus(status, intent);
    }

    /**
     * Analyze which status has occurred and call for specific trigger.
     * It check for start, ok, finish and error if is not of one of them.
     *
     * @param status The status to analyze.
     * @param intent The intent with the extras to send.
     * @see #onStartOperation()
     * @see #onFinishOperation()
     * @see #onResultOK(Intent)
     * @see #onResultError(Intent)
     */
    protected void checkStatus(String status, Intent intent) {
        if (OperationResult.START.name.equals(status)) {
            onStartOperation();
        } else if (OperationResult.FINISH.name.equals(status)) {
            onFinishOperation();
        } else if (OperationResult.OK.name.equals(status)) {
            onResultOK(intent);
        } else {
            onResultError(intent);
        }
    }
    //endregion

    //region Triggers

    /**
     * It is triggered once only when the operation starts.
     */
    protected void onStartOperation() {
        mCallback.onStartOperation();
    }

    /**
     * It is triggered when the response was the expected response.
     * It will be executed one time for each strategy defined.
     */
    protected abstract void onResultOK(Intent anIntent);

    /**
     * It is triggered when an error occurs.
     * It will be executed one time for each strategy defined.
     */
    protected abstract void onResultError(Intent anIntent);

    /**
     * It is triggered once only when the operation was finished (whether successful or not).
     */
    protected void onFinishOperation() {
        mCallback.onFinishOperation();
    }
    //endregion

    //region Register & Unregister

    /**
     * Register the receiver to listen events of the operation and its id.
     *
     * @param aOperation The operation that you want listen.
     * @see #startListening(Class, String)
     */
    public void startListening(Operation aOperation) {
        startListening(aOperation.getClass(), aOperation.getId());
    }

    /**
     * Register the receiver to listen events using a Class without an id.
     *
     * @param aClass The class that you want listen.
     * @see #startListening(Class, String)
     * @see #startListening(Operation)
     */
    public void startListening(Class aClass) {
        startListening(aClass, "");
    }

    /**
     * Register the receiver to listen events using a Class with an id.
     *
     * @param aClass The class that you want listen.
     * @param anId   An identifier that you want listen. It can be empty but not null.
     * @see #startListening(Class, String)
     * @see #startListening(Operation)
     * @see #addFiltersToListen(Class, String, IntentFilter)
     */
    public void startListening(Class aClass, String anId) {
        IntentFilter filter = new IntentFilter();
        addFiltersToListen(aClass, anId, filter);
        LocalBroadcastManager.getInstance(OperationApp.getInstance()).registerReceiver(this, filter);
    }

    /**
     * Add the actions that you want listen to the {@link IntentFilter}.
     * It adds the actions for start, ok, error and finish.
     *
     * @param aClass The class that you want listen.
     * @param anId   An identifier that you want listen. It can be empty but not null.
     * @param filter The {@link IntentFilter} where add the actions.
     * @see OperationBroadcastReceiverHelper#getActionForStart(Class, String)
     * @see OperationBroadcastReceiverHelper#getActionForOk(Class, String)
     * @see OperationBroadcastReceiverHelper#getActionForError(Class, String)
     * @see OperationBroadcastReceiverHelper#getActionForFinish(Class, String)
     */
    protected void addFiltersToListen(Class aClass, String anId, IntentFilter filter) {
        filter.addAction(OperationBroadcastReceiverHelper.getActionForStart(aClass, anId));
        filter.addAction(OperationBroadcastReceiverHelper.getActionForOk(aClass, anId));
        filter.addAction(OperationBroadcastReceiverHelper.getActionForError(aClass, anId));
        filter.addAction(OperationBroadcastReceiverHelper.getActionForFinish(aClass, anId));
    }

    /**
     * Unregister the receiver to stop listening to the events of the operation
     */
    public void stopListening() {
        LocalBroadcastManager.getInstance(OperationApp.getInstance()).unregisterReceiver(this);
    }
    //endregion
}