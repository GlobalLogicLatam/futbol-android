package com.globallogic.futbol.example.operations;

import android.content.Intent;

import com.globallogic.futbol.core.interfaces.IOperationReceiver;
import com.globallogic.futbol.core.interfaces.IOperationStrategy;
import com.globallogic.futbol.core.operation.OperationBroadcastReceiver;
import com.globallogic.futbol.core.operation.strategies.StrategyMock;
import com.globallogic.futbol.example.operations.helper.ExampleOperation;

/**
 * Created by Facundo Mengoni on 6/3/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class TimeOutOperation extends ExampleOperation {
    private static final String TAG = TimeOutOperation.class.getSimpleName();

    @Override
    protected IOperationStrategy getStrategy(Object... arg) {
        StrategyMock strategyMock = new StrategyMock(1.0f);
        strategyMock.addTimeoutException();
        return strategyMock;
    }

    @Override
    public void analyzeResult(int aHttpCode, String result) {
    }

    @Override
    protected void addExtrasForResultOk(Intent intent) {
    }

    public interface ITimeOutReceiver extends IOperationReceiver {
        void onSuccess();

        void onError();
    }

    public static class TimeOutReceiver extends OperationBroadcastReceiver {
        private final ITimeOutReceiver mCallback;

        public TimeOutReceiver(ITimeOutReceiver callback) {
            super(callback);
            mCallback = callback;
        }

        protected void onResultOK(Intent anIntent) {
            mCallback.onSuccess();
        }

        protected void onResultError(Intent anIntent) {
            mCallback.onError();
        }
    }
}