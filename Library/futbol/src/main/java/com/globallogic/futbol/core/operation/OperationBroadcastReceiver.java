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

    public static String getActionForNoInternet(OperationHttp aOperationHttp) {
        return getAction(aOperationHttp.getClass().getSimpleName(),
                OperationResult.NO_INTERNET.name,
                aOperationHttp.getId());
    }

    public static String getActionForStart(OperationHttp aOperationHttp) {
        return getAction(aOperationHttp.getClass().getSimpleName(),
                OperationResult.START.name,
                aOperationHttp.getId());
    }

    public static String getActionForOk(OperationHttp aOperationHttp) {
        return getAction(aOperationHttp.getClass().getSimpleName(),
                OperationResult.OK.name,
                aOperationHttp.getId());
    }

    public static String getActionForError(OperationHttp aOperationHttp) {
        return getAction(aOperationHttp.getClass().getSimpleName(),
                OperationResult.ERROR.name,
                aOperationHttp.getId());
    }

    public static String getActionForFinish(OperationHttp aOperationHttp) {
        return getAction(aOperationHttp.getClass().getSimpleName(),
                OperationResult.FINISH.name,
                aOperationHttp.getId());
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

    public static String getActionForFinish(Class aClass) {
        return getActionForFinish(aClass, "");
    }

    public static String getActionForFinish(Class aClass, String aId) {
        return getAction(aClass.getSimpleName(),
                OperationResult.FINISH.name,
                aId);
    }

    @Override
    public void onReceive(Context aContext, Intent intent) {
        String status = intent.getStringExtra(OperationResult.EXTRA_STATUS);
        if (OperationResult.NO_INTERNET.name.equals(status)) {
            onNoInternet();
        } else if (OperationResult.START.name.equals(status)) {
            onStartOperation();
        } else if (OperationResult.FINISH.name.equals(status)) {
            onFinishOperation();
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
     * Called when the operation is finished. This is after return the result (whether successful or not).
     */
    protected void onFinishOperation() {
    }

    /**
     * Register the receiver to listen events of the operation
     *
     * @param aOperationHttp the operation to listen
     */
    public void register(OperationHttp aOperationHttp) {
        register(aOperationHttp.getClass(), aOperationHttp.getId());
    }

    public void register(OperationFile aOperationFile) {
        register(aOperationFile.getClass(), aOperationFile.getId());
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
        filter.addAction(getActionForNoInternet(aClass, aId));
        filter.addAction(getActionForStart(aClass, aId));
        filter.addAction(getActionForOk(aClass, aId));
        filter.addAction(getActionForError(aClass, aId));
        filter.addAction(getActionForFinish(aClass, aId));
        LocalBroadcastManager.getInstance(OperationApp.getInstance()).registerReceiver(this, filter);
    }

    /**
     * Unregister the receiver to stop listening to the events of the operation
     */
    public void unRegister() {
        LocalBroadcastManager.getInstance(OperationApp.getInstance()).unregisterReceiver(this);
    }
}