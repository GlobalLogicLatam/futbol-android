package com.globallogic.futbol.strategies.ion.example.operations;

import android.content.Intent;

import com.globallogic.futbol.core.broadcasts.OperationHttpBroadcastReceiver;
import com.globallogic.futbol.core.interfaces.callbacks.IStrategyHttpCallback;
import com.globallogic.futbol.core.operations.OperationHelper;
import com.globallogic.futbol.core.responses.StrategyHttpResponse;
import com.globallogic.futbol.core.strategies.OperationStrategy;
import com.globallogic.futbol.core.strategies.mock.StrategyHttpMock;
import com.globallogic.futbol.strategies.ion.StrategyIonSingleStringDelete;
import com.globallogic.futbol.strategies.ion.StrategyIonSingleStringPost;
import com.globallogic.futbol.strategies.ion.example.BuildConfig;
import com.globallogic.futbol.strategies.ion.example.entities.Device;
import com.globallogic.futbol.strategies.ion.example.operations.helper.ExampleOperation;
import com.google.gson.JsonObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by Facundo Mengoni on 8/6/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class DeleteDeviceOperation extends ExampleOperation {
    private static final String TAG = DeleteDeviceOperation.class.getSimpleName();

    private Boolean mock = false || BuildConfig.MOCK;

    private String mUrl = "http://172.17.201.125:1337/device/%s";

    public void execute(String id) {
        performOperation(id);
    }

    @Override
    protected ArrayList<OperationStrategy> getStrategies(Object... arg) {
        String id = (String) arg[0];

        ArrayList<OperationStrategy> strategies = new ArrayList<>();
        BaseHttpAnalyzer analyzer = new BaseHttpAnalyzer() {
            private Device mDevice;
            private boolean mNotFound;

            @Override
            public Boolean analyzeResult(Integer aHttpCode, String aString) {
                switch (aHttpCode) {
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        this.mNotFound = true;
                        return true;
                    case HttpURLConnection.HTTP_OK:
                        this.mDevice = OperationHelper.getModelObject(aString, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Device.class);
                        return true;
                }
                return false;
            }

            @Override
            public void addExtrasForResultOk(Intent intent) {
                if (mNotFound)
                    intent.putExtra(DeleteDeviceReceiver.EXTRA_NOT_FOUND, true);
                else
                    intent.putExtra(DeleteDeviceReceiver.EXTRA_DEVICE, mDevice);
            }
        };

        if (mock) {
            StrategyHttpMock strategyMock = new StrategyHttpMock(this, analyzer, 0f);
            strategyMock.add(new StrategyHttpResponse(HttpURLConnection.HTTP_NOT_FOUND, ""));
            strategyMock.add(new StrategyHttpResponse(HttpURLConnection.HTTP_OK, "{\"createdAt\":\"2015-08-05T11:14:45.374Z\",\"id\":\"1\",\"name\":\"S3\",\"resolution\":\"720x1280\",\"updatedAt\":\"2015-08-05T11:14:45.374Z\"}"));
            strategyMock.add(new StrategyHttpResponse(HttpURLConnection.HTTP_OK, "{\"createdAt\":\"2015-08-05T11:14:45.374Z\",\"id\":\"" + id + "\",\"name\":\"S3\",\"resolution\":\"720x1280\",\"updatedAt\":\"2015-08-05T11:14:45.374Z\"}"));
            strategies.add(strategyMock);
        } else {
            StrategyIonSingleStringDelete strategy = new StrategyIonSingleStringDelete(this, analyzer, String.format(mUrl, id));
            strategies.add(strategy);
        }
        return strategies;
    }

    public interface IDeleteDeviceReceiver extends IStrategyHttpCallback {
        void onSuccess(Device aDevice);

        void onError();

        void onNotFound();
    }

    public static class DeleteDeviceReceiver extends OperationHttpBroadcastReceiver {
        static final String EXTRA_NOT_FOUND = "EXTRA_NOT_FOUND";
        static final String EXTRA_DEVICE = "EXTRA_DEVICE";
        private final IDeleteDeviceReceiver mCallback;

        public DeleteDeviceReceiver(IDeleteDeviceReceiver callback) {
            super(callback);
            mCallback = callback;
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