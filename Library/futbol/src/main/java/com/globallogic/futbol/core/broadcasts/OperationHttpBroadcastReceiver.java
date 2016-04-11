package com.globallogic.futbol.core.broadcasts;

import android.content.Intent;
import android.content.IntentFilter;

import com.globallogic.futbol.core.OperationResult;
import com.globallogic.futbol.core.interfaces.callbacks.IStrategyHttpCallback;

/**
 * {@inheritDoc}<br>
 * Also support the case of no internet.
 *
 * @see IStrategyHttpCallback
 */
public abstract class OperationHttpBroadcastReceiver extends OperationBroadcastReceiver {
    //region Variables
    private final IStrategyHttpCallback mCallback;
    //endregion

    //region Constructors implementation
    protected OperationHttpBroadcastReceiver(IStrategyHttpCallback callback) {
        super(callback);
        this.mCallback = callback;
    }
    //endregion

    //region Triggers

    /**
     * It is triggered when the strategy can't start because don't have connection
     */
    protected void onNoInternet() {
        mCallback.onNoInternet();
    }
    //endregion

    //region BroadcastReceiver implementation

    /**
     * {@inheritDoc}<br>
     * Also check the no internet status.
     *
     * @see #onResultError(Intent)
     */
    @Override
    protected void checkStatus(String status, Intent intent) {
        if (OperationResult.NO_INTERNET.name.equals(status)) {
            onNoInternet();
        } else {
            super.checkStatus(status, intent);
        }
    }

    /**
     * {@inheritDoc}<br>
     * Also add the action for no internet.
     *
     * @see OperationBroadcastReceiverHelper#getActionForNoInternet(Class, String)
     */
    @Override
    protected void addFiltersToListen(Class aClass, String aId, IntentFilter filter) {
        super.addFiltersToListen(aClass, aId, filter);
        filter.addAction(OperationBroadcastReceiverHelper.getActionForNoInternet(aClass, aId));
    }
    //endregion
}