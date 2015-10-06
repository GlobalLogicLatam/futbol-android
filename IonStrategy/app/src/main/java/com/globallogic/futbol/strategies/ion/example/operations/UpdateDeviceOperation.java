package com.globallogic.futbol.strategies.ion.example.operations;

import android.content.Intent;

import com.globallogic.futbol.core.interfaces.IOperationStrategy;
import com.globallogic.futbol.core.operation.OperationBroadcastReceiver;
import com.globallogic.futbol.core.operation.OperationHelper;
import com.globallogic.futbol.core.operation.strategies.StrategyMock;
import com.globallogic.futbol.core.operation.strategies.StrategyMockResponse;
import com.globallogic.futbol.strategies.ion.StrategyIonSingleStringPost;
import com.globallogic.futbol.strategies.ion.example.BuildConfig;
import com.globallogic.futbol.strategies.ion.example.entities.Device;
import com.globallogic.futbol.strategies.ion.example.operations.helper.ExampleOperation;
import com.google.gson.JsonObject;

import java.net.HttpURLConnection;

/**
 * Created by Facundo Mengoni on 8/6/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class UpdateDeviceOperation extends ExampleOperation {
    private static final String TAG = UpdateDeviceOperation.class.getSimpleName();

    private Boolean mock = false || BuildConfig.MOCK;

    private String mUrl = "http://172.17.201.125:1337/device/%s";
    private Device mDevice;
    private boolean mNotFound;

    @Override
    public void reset() {
        super.reset();
        mDevice = null;
        mNotFound = false;
    }

    public void execute (String id, String name, String resolution){
        reset();
        performOperation(id, name, resolution);
    }

    @Override
    protected IOperationStrategy getStrategy(Object... arg) {
        String id = (String) arg[0];
        String name = (String) arg[1];
        String resolution = (String) arg[2];

        if (mock) {
            StrategyMock strategyMock = new StrategyMock(0f);
            strategyMock.add(new StrategyMockResponse(HttpURLConnection.HTTP_NOT_FOUND, ""));
            strategyMock.add(new StrategyMockResponse(HttpURLConnection.HTTP_OK, "{\"createdAt\":\"2015-08-05T11:14:45.374Z\",\"id\":\"1\",\"name\":\"S3\",\"resolution\":\"720x1280\",\"updatedAt\":\"2015-08-05T11:14:45.374Z\"}"));
            strategyMock.add(new StrategyMockResponse(HttpURLConnection.HTTP_OK, "{\"createdAt\":\"2015-08-05T11:14:45.374Z\",\"id\":\"" + id + "\",\"name\":\"" + name + "\",\"resolution\":\"" + resolution + "\",\"updatedAt\":\"2015-08-05T11:14:45.374Z\"}"));
            return strategyMock;
        }

        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("resolution", resolution);
        StrategyIonSingleStringPost strategy = new StrategyIonSingleStringPost(String.format(mUrl, id), json.toString());
        return strategy;
    }

    @Override
    public Boolean analyzeResult(int aHttpCode, String result) {
        switch (aHttpCode) {
            case HttpURLConnection.HTTP_NOT_FOUND:
                this.mNotFound = true;
                return true;
            case HttpURLConnection.HTTP_OK:
                this.mDevice = OperationHelper.getModelObject(result, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Device.class);
                return true;
        }
        return false;
    }

    @Override
    protected void addExtrasForResultOk(Intent intent) {
        if (mNotFound)
            intent.putExtra(UpdateDeviceReceiver.EXTRA_NOT_FOUND, true);
        else
            intent.putExtra(UpdateDeviceReceiver.EXTRA_DEVICE, mDevice);
    }

    public interface IUpdateDeviceReceiver {
        void onNoInternet();

        void onStartOperation();

        void onSuccess(Device aDevice);

        void onError();

        void onNotFound();
    }

    public static class UpdateDeviceReceiver extends OperationBroadcastReceiver {
        static final String EXTRA_NOT_FOUND = "EXTRA_NOT_FOUND";
        static final String EXTRA_DEVICE = "EXTRA_DEVICE";
        private final IUpdateDeviceReceiver mCallback;

        public UpdateDeviceReceiver(IUpdateDeviceReceiver callback) {
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
            boolean notFoundFound = anIntent.getBooleanExtra(EXTRA_NOT_FOUND, false);
            if (!notFoundFound) {
                Device device = (Device) anIntent.getSerializableExtra(EXTRA_DEVICE);
                if (device != null)
                    mCallback.onSuccess(device);
                else
                    mCallback.onError();
            } else
                mCallback.onNotFound();
        }

        protected void onResultError(Intent anIntent) {
            mCallback.onError();
        }
    }
}