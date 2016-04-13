package com.globallogic.futbol.strategies.ion.example.operations;

import android.content.Intent;

import com.globallogic.futbol.core.broadcasts.OperationHttpBroadcastReceiver;
import com.globallogic.futbol.core.interfaces.callbacks.IStrategyHttpCallback;
import com.globallogic.futbol.core.strategies.OperationStrategy;
import com.globallogic.futbol.core.strategies.mock.StrategyHttpMock;
import com.globallogic.futbol.strategies.ion.StrategyIonConfig;
import com.globallogic.futbol.strategies.ion.StrategyIonSingleStringGet;
import com.globallogic.futbol.strategies.ion.example.BuildConfig;
import com.globallogic.futbol.strategies.ion.example.operations.helper.ExampleOperation;

import java.util.ArrayList;

/**
 * Created by Facundo Mengoni on 6/3/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class TimeOutOperation extends ExampleOperation {
    private static final String TAG = TimeOutOperation.class.getSimpleName();

    private Boolean mock = false || BuildConfig.MOCK;

    private String mUrl = "http://www.google.com:81/";

    public void execute() {
        performOperation();
    }

    @Override
    protected ArrayList<OperationStrategy> getStrategies(Object... arg) {
        ArrayList<OperationStrategy> strategies = new ArrayList<OperationStrategy>();
        BaseHttpAnalyzer analyzer = new BaseHttpAnalyzer() {
            @Override
            public Boolean analyzeResult(Integer aHttpCode, String aString) {
                return true;
            }

            @Override
            public void addExtrasForResultOk(Intent intent) {
            }
        };

        if (mock) {
            StrategyHttpMock strategyHttpMock = new StrategyHttpMock(this, analyzer, 1.0f);
            strategyHttpMock.addTimeoutException();
            strategies.add(strategyHttpMock);
        } else {
            StrategyIonSingleStringGet strategy = new StrategyIonSingleStringGet(this, analyzer, new StrategyIonConfig(0, 10), mUrl);
            strategies.add(strategy);
        }
        return strategies;
    }

    public interface ITimeOutReceiver extends IStrategyHttpCallback {
        void onSuccess();

        void onError();
    }

    public static class TimeOutReceiver extends OperationHttpBroadcastReceiver {
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