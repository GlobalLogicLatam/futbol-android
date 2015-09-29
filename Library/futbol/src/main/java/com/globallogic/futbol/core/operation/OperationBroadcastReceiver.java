package com.globallogic.futbol.core.operation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.globallogic.futbol.core.LocalBroadcastManager;
import com.globallogic.futbol.core.OperationApp;
import com.globallogic.futbol.core.interfaces.IOperationReceiver;

public abstract class OperationBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_FORMAT = "action:%s_%s_%s";
    protected static final String EXTRA_EXCEPTION = "EXTRA_EXCEPTION";
    private static final String TAG = OperationBroadcastReceiver.class.getSimpleName();
    private final IOperationReceiver mCallback;

    protected OperationBroadcastReceiver(IOperationReceiver mCallback) {
        this.mCallback = mCallback;
    }

    public static String getAction(String aClazz, String anAction, String anId) {
        return String.format(ACTION_FORMAT, aClazz, anAction, anId);
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

    public static String getActionForFinish(Operation aOperation) {
        return getAction(aOperation.getClass().getSimpleName(),
                OperationResult.FINISH.name,
                aOperation.getId());
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
        if (OperationResult.START.name.equals(status)) {
            mCallback.onStartOperation();
        } else if (OperationResult.FINISH.name.equals(status)) {
            onFinishOperation();
        } else if (OperationResult.OK.name.equals(status)) {
            onResultOK(intent);
        } else {
            onResultError(intent);
        }
    }

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
    protected void onFinishOperation(){
    }

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