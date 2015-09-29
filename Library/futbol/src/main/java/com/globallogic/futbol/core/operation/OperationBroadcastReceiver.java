package com.globallogic.futbol.core.operation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.globallogic.futbol.core.LocalBroadcastManager;
import com.globallogic.futbol.core.OperationApp;

public abstract class OperationBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_FORMAT = "action:%s_%s_%s";
    protected static final String EXTRA_EXCEPTION = "EXTRA_EXCEPTION";
    private static final String TAG = OperationBroadcastReceiver.class.getSimpleName();

    protected OperationBroadcastReceiver() {
    }

    public static String getAction(String aClazz, String anAction, String anId) {
        return String.format(ACTION_FORMAT, aClazz, anAction, anId);
    }

    public static String getActionForNoInternet(Operation aOperation) {
        return getAction(aOperation.getClass().getSimpleName(),
                OperationResult.NO_INTERNET.name,
                aOperation.getId());
    }

    public static String getActionForStart(Operation aOperation) {
        return getAction(aOperation.getClass().getSimpleName(),
                OperationResult.START.name,
                aOperation.getId());
    }

    public static String getActionForOk(Operation aOperation) {
        return getAction(aOperation.getClass().getSimpleName(),
                OperationResult.OK.name,
                aOperation.getId());
    }

    public static String getActionForError(Operation aOperation) {
        return getAction(aOperation.getClass().getSimpleName(),
                OperationResult.ERROR.name,
                aOperation.getId());
    }

    public static String getActionForNoInternet(Class aClass) {
        return getActionForNoInternet(aClass, "");
    }

    public static String getActionForNoInternet(Class aClass, String aId) {
        return getAction(aClass.getSimpleName(),
                OperationResult.NO_INTERNET.name,
                aId);
    }

    public static String getActionForStart(Class aClass) {
        return getActionForStart(aClass, "");
    }

    public static String getActionForStart(Class aClass, String aId) {
        return getAction(aClass.getSimpleName(),
                OperationResult.START.name,
                aId);
    }

    public static String getActionForOk(Class aClass) {
        return getActionForOk(aClass, "");
    }

    public static String getActionForOk(Class aClass, String aId) {
        return getAction(aClass.getSimpleName(),
                OperationResult.OK.name,
                aId);
    }

    public static String getActionForError(Class aClass) {
        return getActionForError(aClass, "");
    }

    public static String getActionForError(Class aClass, String aId) {
        return getAction(aClass.getSimpleName(),
                OperationResult.ERROR.name,
                aId);
    }

    @Override
    public void onReceive(Context aContext, Intent intent) {
        String status = intent.getStringExtra(OperationResult.EXTRA_STATUS);
        if (OperationResult.NO_INTERNET.name.equals(status)) {
            onNoInternet();
        } else if (OperationResult.START.name.equals(status)) {
            onStartOperation();
        } else if (OperationResult.OK.name.equals(status)) {
            onResultOK(intent);
        } else {
            onResultError(intent);
        }
    }

    /**
     * It is triggered when the operation can't start because don't have connection
     */
    protected abstract void onNoInternet();

    /**
     * It is triggered when the operation starts
     */
    protected abstract void onStartOperation();

    /**
     * It is triggered when it was possible to connect to the server
     */
    protected abstract void onResultOK(Intent anIntent);

    /**
     * It is triggered when an error occurred when connecting to the server
     */
    protected abstract void onResultError(Intent anIntent);

    /**
     * Register the receiver to listen events of the operation
     *
     * @param aOperation the operation to listen
     */
    public void register(Operation aOperation) {
        register(aOperation.getClass(), aOperation.getId());
    }

    /**
     * Register the receiver to listen events of the operation
     *
     * @param aClass the {@link Class} of operation to listen
     */
    public void register(Class aClass) {
        register(aClass, "");
    }

    /**
     * Register the receiver to listen events of the operation
     *
     * @param aClass the {@link Class} of operation to listen
     * @param aId    the id of operation to listen
     */
    public void register(Class aClass, String aId) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(getActionForStart(aClass, aId));
        filter.addAction(getActionForOk(aClass, aId));
        filter.addAction(getActionForError(aClass, aId));
        LocalBroadcastManager.getInstance(OperationApp.getInstance()).registerReceiver(this, filter);
    }

    /**
     * Unregister the receiver to stop listening to the events of the operation
     */
    public void unRegister() {
        LocalBroadcastManager.getInstance(OperationApp.getInstance()).unregisterReceiver(this);
    }
}