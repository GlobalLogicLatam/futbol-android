package com.globallogic.futbol.demo.app.receivers;

import android.content.Intent;

import com.globallogic.futbol.core.broadcasts.OperationBroadcastReceiver;
import com.globallogic.futbol.demo.R;
import com.globallogic.futbol.demo.app.interfaces.MyStrategyHttpCallback;

/**
 * Default receiver for the demo
 * @author facundo.mengoni
 */
public abstract class MyReceiver extends OperationBroadcastReceiver {
    public static final String ERROR = "ERROR";
    private MyStrategyHttpCallback mCallback;

    protected MyReceiver(MyStrategyHttpCallback callback) {
        super(callback);
        this.mCallback = callback;
    }

    @Override
    protected void onResultError(Intent intent) {
        mCallback.onError(intent.getIntExtra(ERROR, R.string.unexpected_response_exception));
    }
}
