package com.globallogic.futbol.demo.app.operations;

import android.content.Context;
import android.content.Intent;

import com.globallogic.futbol.core.interfaces.IOperationReceiver;
import com.globallogic.futbol.core.interfaces.IOperationStrategy;
import com.globallogic.futbol.core.operation.Operation;
import com.globallogic.futbol.core.operation.OperationBroadcastReceiver;
import com.globallogic.futbol.core.operation.OperationHelper;
import com.globallogic.futbol.core.operation.strategies.StrategyMock;
import com.globallogic.futbol.core.operation.strategies.StrategyMockResponse;
import com.globallogic.futbol.demo.R;
import com.globallogic.futbol.demo.domain.Device;
import com.globallogic.futbol.strategies.ion.StrategyIonSingleStringGet;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Facundo Mengoni.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class GetDevicesOperation extends MyOperation {
    private final String URL = "http://172.17.201.125:1337/device/";

    private ArrayList<Device> mList;
    private boolean mock = false;

    @Override
    public void reset() {
        super.reset();
        mList = null;
    }

    @Override
    protected IOperationStrategy getStrategy(Object... args) {
        if (mock) {
            StrategyMock strategy = new StrategyMock(0f);
            strategy.add(new StrategyMockResponse(200, "[{\"id\":1,\"name\":\"S3\",\"resolution\":\"720x1280\",\"createdAt\":\"2015-08-06T13:19:39.000Z\",\"updatedAt\":\"2015-08-06T13:21:03.000Z\"}]"));
            return strategy;
        }

        StrategyIonSingleStringGet strategy = new StrategyIonSingleStringGet(URL);
        return strategy;
    }

    @Override
    public void analyzeResult(int httpCode, String result) {
        switch (httpCode) {
            case HttpURLConnection.HTTP_OK:
                this.mList = OperationHelper.getModelArray(result, Device.class);
                break;
            case HttpURLConnection.HTTP_BAD_REQUEST:
                this.mError = R.string.bad_request;
                break;
            case HttpURLConnection.HTTP_INTERNAL_ERROR:
                this.mError = R.string.internal_error;
                break;
            default:
                this.mError = R.string.other_exception;
                break;
        }
    }

    @Override
    protected void addExtrasForResultOk(Intent intent) {
        intent.putExtra(GetDevicesReceiver.DEVICES, mList);
    }

    public interface IGetDevicesOperation extends IOperationReceiver {
        void onSuccess(ArrayList<Device> devices);

        void onError();
    }

    public static class GetDevicesReceiver extends OperationBroadcastReceiver {
        public static final String DEVICES = "DEVICES";

        private final IGetDevicesOperation mCallback;

        public GetDevicesReceiver(IGetDevicesOperation mCallback) {
            super(mCallback);
            this.mCallback = mCallback;
        }

        @Override
        protected void onResultOK(Intent intent) {
            ArrayList<Device> devices = (ArrayList<Device>) intent.getSerializableExtra(DEVICES);
            mCallback.onSuccess(devices);
        }

        @Override
        protected void onResultError(Intent intent) {
            mCallback.onError();
        }
    }
}