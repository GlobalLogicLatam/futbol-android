package com.globallogic.futbol.demo.app.operations;

import android.content.Context;
import android.content.Intent;

import com.globallogic.futbol.core.operations.OperationHelper;
import com.globallogic.futbol.core.responses.StrategyHttpResponse;
import com.globallogic.futbol.core.strategies.OperationStrategy;
import com.globallogic.futbol.core.strategies.mock.StrategyHttpMock;
import com.globallogic.futbol.demo.R;
import com.globallogic.futbol.demo.app.analyzers.MyDefaultHttpAnalyzer;
import com.globallogic.futbol.demo.app.interfaces.MyStrategyHttpCallback;
import com.globallogic.futbol.demo.app.receivers.MyReceiver;
import com.globallogic.futbol.demo.domain.Device;
import com.globallogic.futbol.strategies.ion.StrategyIonSingleStringPost;
import com.google.gson.JsonObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by Facundo Mengoni.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class CreateDeviceOperation extends MyOperation {
    private final String URL = "http://172.17.201.125:1337/device/";

    private boolean mock = false;

    @Override
    protected ArrayList<OperationStrategy> getStrategies(Object... args) {
        String name = (String) args[0];
        String resolution = (String) args[1];
        ArrayList<OperationStrategy> strategies = new ArrayList<>();
        MyDefaultHttpAnalyzer analyzer = new MyDefaultHttpAnalyzer() {
            private Device mDevice;

            @Override
            public Boolean analyzeResult(Integer aHttpCode, String aString) {
                switch (aHttpCode) {
                    case HttpURLConnection.HTTP_CREATED:
                    case HttpURLConnection.HTTP_OK:
                        this.mDevice = OperationHelper.getModelObject(aString, Device.class);
                        return true;
                    case HttpURLConnection.HTTP_BAD_REQUEST:
                        this.mError = R.string.bad_request;
                        return true;
                    case HttpURLConnection.HTTP_INTERNAL_ERROR:
                        this.mError = R.string.internal_error;
                        return true;
                }
                return false;
            }

            @Override
            public void addExtrasForResultOk(Intent intent) {
                intent.putExtra(CreateDeviceReceiver.DEVICE, mDevice);
            }
        };

        if (mock) {
            StrategyHttpMock strategy = new StrategyHttpMock(this, analyzer, 0f);
            strategy.add(new StrategyHttpResponse(200, "{\"id\":1,\"name\":\"" + name + "\",\"resolution\":\"" + resolution + "\",\"createdAt\":\"2015-08-06T13:19:39.000Z\",\"updatedAt\":\"2015-08-06T13:21:03.000Z\"}"));
            strategies.add(strategy);
        } else {
            JsonObject json = new JsonObject();
            json.addProperty("name", name);
            json.addProperty("resolution", resolution);
            StrategyIonSingleStringPost strategy = new StrategyIonSingleStringPost(this, analyzer, URL, json.toString());
            strategies.add(strategy);
        }
        return strategies;
    }

    public interface ICreateDeviceOperation extends MyStrategyHttpCallback {
        void onSuccess(Device device);
    }

    public static class CreateDeviceReceiver extends MyReceiver {
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
                onResultError(intent);
        }
    }
}