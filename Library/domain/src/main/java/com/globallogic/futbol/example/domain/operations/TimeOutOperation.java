package com.globallogic.futbol.example.domain.operations;

import android.content.Intent;

import com.globallogic.futbol.core.interfaces.callbacks.IStrategyHttpCallback;
import com.globallogic.futbol.core.broadcasts.OperationHttpBroadcastReceiver;
import com.globallogic.futbol.core.strategies.OperationStrategy;
import com.globallogic.futbol.core.strategies.mock.StrategyHttpMock;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Facundo Mengoni on 6/3/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class TimeOutOperation extends BaseOperation {
    private static final String TAG = TimeOutOperation.class.getSimpleName();

    public void execute() {
        performOperation();
    }

    @Override
    protected ArrayList<OperationStrategy> getStrategies(Object... arg) {
        StrategyHttpMock strategyHttpMock = new StrategyHttpMock(this, new BaseHttpAnalyzer() {
            @Override
            public Boolean analyzeResult(Integer aHttpCode, String aString) {
                return true;
            }

            @Override
            public void addExtrasForResultOk(Intent intent) {

            }
        }, 1.0f);
        strategyHttpMock.addTimeoutException();
        return new ArrayList<OperationStrategy>(Collections.singletonList(strategyHttpMock));
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