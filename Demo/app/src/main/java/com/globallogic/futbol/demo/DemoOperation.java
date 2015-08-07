package com.globallogic.futbol.demo;

import android.content.Context;
import android.content.Intent;

import com.globallogic.futbol.core.interfaces.IOperationReceiver;
import com.globallogic.futbol.core.interfaces.IOperationStrategy;
import com.globallogic.futbol.core.operation.Operation;
import com.globallogic.futbol.core.operation.OperationBroadcastReceiver;
import com.globallogic.futbol.core.operation.OperationHelper;
import com.globallogic.futbol.core.operation.strategies.StrategyMock;
import com.globallogic.futbol.core.operation.strategies.StrategyMockResponse;

import java.net.HttpURLConnection;

/**
 * Created by Facundo Mengoni.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class DemoOperation extends Operation {
    private int mError = R.string.empty_error;
    private DemoModel mModel;

    @Override
    public void reset() {
        super.reset();
        mError = R.string.empty_error;
        mModel = null;
    }

    @Override
    protected IOperationStrategy getStrategy(Object... args) {
        String id = (String) args[0];

        StrategyMock strategy = new StrategyMock(0f);
        strategy.add(new StrategyMockResponse(200, "{\"id\":" + id + ",\"name\":\"Facundo\"}"));
        strategy.add(new StrategyMockResponse(200, "{\"id\":" + id + ",\"name\":\"Ezequiel\"}"));
        strategy.add(new StrategyMockResponse(200, "{\"id\":" + id + ",\"name\":\"Fernando\"}"));
        return strategy;
    }

    @Override
    public void analyzeResult(int httpCode, String result) {
        switch (httpCode) {
            case HttpURLConnection.HTTP_OK:
                this.mModel = OperationHelper.getModelObject(result, DemoModel.class);
                break;
        }
    }

    @Override
    public void analyzeException(Exception e) {
        OperationHelper.analyzeException(e, new OperationHelper.ExceptionCallback() {
            @Override
            public void jsonSyntaxException() {
                mError = R.string.json_syntax_exception;
            }

            @Override
            public void timeOutException() {
                mError = R.string.time_out_exception;
            }

            @Override
            public void socketException() {
                mError = R.string.socket_exception;
            }

            @Override
            public void malformedURLException() {
                mError = R.string.malformed_url_exception;
            }

            @Override
            public void ioException() {
                mError = R.string.io_exception;
            }

            @Override
            public void otherException() {
                mError = R.string.other_exception;
            }
        });
    }

    @Override
    protected void addExtrasForResultOk(Intent intent) {
        if (mModel != null)
            intent.putExtra(DemoReceiver.DEMO_MODEL, mModel);
    }

    @Override
    protected void addExtrasForResultError(Intent intent) {
        // Nothing in this case
    }

    @Override
    public String getError(Context context) {
        return context.getString(mError);
    }

    public interface IDemoOperation extends IOperationReceiver {
        void onSuccess(DemoModel model);

        void onError();

        void onNotFound();
    }

    public static class DemoReceiver extends OperationBroadcastReceiver {
        public static final String DEMO_MODEL = "DEMO_MODEL";

        private final IDemoOperation mCallback;

        public DemoReceiver(IDemoOperation mCallback) {
            super(mCallback);
            this.mCallback = mCallback;
        }

        @Override
        protected void onResultOK(Intent intent) {
            DemoModel demoModel = (DemoModel) intent.getSerializableExtra(DEMO_MODEL);
            if (demoModel != null)
                mCallback.onSuccess(demoModel);
            else
                mCallback.onNotFound();
        }

        @Override
        protected void onResultError(Intent intent) {
            mCallback.onError();
        }
    }
}