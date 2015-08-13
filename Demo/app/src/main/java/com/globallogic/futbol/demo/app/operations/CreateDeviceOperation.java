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
import com.globallogic.futbol.strategies.ion.StrategyIonSingleStringPost;
import com.google.gson.JsonObject;

import java.net.HttpURLConnection;

/**
 * Created by Facundo Mengoni.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class CreateDeviceOperation extends MyOperation {
    private final String URL = "http://172.17.201.125:1337/device/";

    private Device mDevice;
    private boolean mock = false;

    @Override
    public void reset() {
        super.reset();
        mDevice = null;
    }

    @Override
    protected IOperationStrategy getStrategy(Object... args) {
        String name = (String) args[0];
        String resolution = (String) args[1];

        if (mock) {
            StrategyMock strategy = new StrategyMock(0f);
            strategy.add(new StrategyMockResponse(200, "{\"id\":1,\"name\":\"" + name + "\",\"resolution\":\"" + resolution + "\",\"createdAt\":\"2015-08-06T13:19:39.000Z\",\"updatedAt\":\"2015-08-06T13:21:03.000Z\"}"));
            return strategy;
        }

        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("resolution", resolution);
        StrategyIonSingleStringPost strategy = new StrategyIonSingleStringPost(URL, json.toString());
        return strategy;
    }

    @Override
    public void analyzeResult(int httpCode, String result) {
        switch (httpCode) {
            case HttpURLConnection.HTTP_CREATED:
            case HttpURLConnection.HTTP_OK:
                this.mDevice = OperationHelper.getModelObject(result, Device.class);
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
        intent.putExtra(CreateDeviceReceiver.DEVICE, mDevice);
    }

    public interface ICreateDeviceOperation extends IOperationReceiver {
        void onSuccess(Device device);

        void onError();
    }

    public static class CreateDeviceReceiver extends OperationBroadcastReceiver {
        public static final String DEVICE = "DEVICE";

        private final ICreateDeviceOperation mCallback;

        public CreateDeviceReceiver(ICreateDeviceOperation mCallback) {
            super(mCallback);
            this.mCallback = mCallback;
        }

        @Override
        protected void onResultOK(Intent intent) {
            Device device = (Device) intent.getSerializableExtra(DEVICE);
            if (device != null)
                mCallback.onSuccess(device);
            else
                mCallback.onError();
        }

        @Override
        protected void onResultError(Intent intent) {
            mCallback.onError();
        }
    }
}