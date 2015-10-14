package com.globallogic.futbol.example.operations;

import android.content.Intent;

import com.globallogic.futbol.core.OperationResponse;
import com.globallogic.futbol.core.interfaces.IOperationStrategy;
import com.globallogic.futbol.core.operation.OperationBroadcastReceiver;
import com.globallogic.futbol.core.operation.strategies.StrategyHttpMock;
import com.globallogic.futbol.example.operations.helper.ExampleOperationHttp;

/**
 * Created by Facundo Mengoni on 6/3/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class TimeOutOperationHttp extends ExampleOperationHttp {
    private static final String TAG = TimeOutOperationHttp.class.getSimpleName();

    public void execute(){
        performOperation();
    }

    @Override
    protected IOperationStrategy getStrategy(Object... arg) {
        StrategyHttpMock strategyHttpMock = new StrategyHttpMock(1.0f);
        strategyHttpMock.addTimeoutException();
        return strategyHttpMock;
    }

    @Override
    public Boolean analyzeResult(OperationResponse<Integer, String> response) {
        return true;
    }

    @Override
    protected void addExtrasForResultOk(Intent intent) {
    }

    public interface ITimeOutReceiver {
        void onNoInternet();

        void onStartOperation();

        void onSuccess();

        void onError();
    }

    public static class TimeOutReceiver extends OperationBroadcastReceiver {
        private final ITimeOutReceiver mCallback;

        public TimeOutReceiver(ITimeOutReceiver callback) {
            super();
            mCallback = callback;
        }

        @Override
        protected void onNoInternet() {
            mCallback.onNoInternet();
        }

        @Override
        protected void onStartOperation() {
            mCallback.onStartOperation();
        }

        protected void onResultOK(Intent anIntent) {
            mCallback.onSuccess();
        }

        protected void onResultError(Intent anIntent) {
            mCallback.onError();
        }
    }
}