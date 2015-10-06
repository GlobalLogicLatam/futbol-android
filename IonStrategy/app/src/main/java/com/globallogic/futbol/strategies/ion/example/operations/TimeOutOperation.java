package com.globallogic.futbol.strategies.ion.example.operations;

import android.content.Intent;

import com.globallogic.futbol.core.interfaces.IOperationStrategy;
import com.globallogic.futbol.core.operation.OperationBroadcastReceiver;
import com.globallogic.futbol.core.operation.strategies.StrategyMock;
import com.globallogic.futbol.strategies.ion.StrategyIonConfig;
import com.globallogic.futbol.strategies.ion.StrategyIonSingleStringGet;
import com.globallogic.futbol.strategies.ion.example.BuildConfig;
import com.globallogic.futbol.strategies.ion.example.operations.helper.ExampleOperation;

/**
 * Created by Facundo Mengoni on 6/3/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class TimeOutOperation extends ExampleOperation {
    private static final String TAG = TimeOutOperation.class.getSimpleName();

    private Boolean mock = false || BuildConfig.MOCK;

    private String mUrl = "http://www.google.com:81/";

    public void execute (){
        reset();
        performOperation();
    }

    @Override
    protected IOperationStrategy getStrategy(Object... arg) {
        if (mock) {
            StrategyMock strategyMock = new StrategyMock(1.0f);
            strategyMock.addTimeoutException();
            return strategyMock;
        }
        StrategyIonSingleStringGet strategy = new StrategyIonSingleStringGet(new StrategyIonConfig(0, 10), mUrl);
        return strategy;
    }

    @Override
    public Boolean analyzeResult(int aHttpCode, String result) {
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