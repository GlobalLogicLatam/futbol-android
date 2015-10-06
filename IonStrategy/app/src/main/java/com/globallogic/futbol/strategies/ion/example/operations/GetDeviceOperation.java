package com.globallogic.futbol.strategies.ion.example.operations;

import android.content.Intent;

import com.globallogic.futbol.core.interfaces.IOperationStrategy;
import com.globallogic.futbol.core.operation.OperationBroadcastReceiver;
import com.globallogic.futbol.core.operation.OperationHelper;
import com.globallogic.futbol.core.operation.strategies.StrategyMock;
import com.globallogic.futbol.core.operation.strategies.StrategyMockResponse;
import com.globallogic.futbol.strategies.ion.StrategyIonSingleStringGet;
import com.globallogic.futbol.strategies.ion.example.BuildConfig;
import com.globallogic.futbol.strategies.ion.example.entities.Device;
import com.globallogic.futbol.strategies.ion.example.operations.helper.ExampleOperation;

import java.net.HttpURLConnection;

/**
 * Created by Facundo Mengoni on 8/6/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class GetDeviceOperation extends ExampleOperation {
    private static final String TAG = GetDeviceOperation.class.getSimpleName();

    private Boolean mock = false || BuildConfig.MOCK;

    private String mUrl = "http://172.17.201.125:1337/device/%s";
    private final String mId;
    private Device mDevice;

    public GetDeviceOperation(String anId) {
        super(anId);
        mId = anId;
    }

    @Override
    public void reset() {
        super.reset();
        mDevice = null;
    }

    public void execute () {
        performOperation();
    }

    @Override
    protected IOperationStrategy getStrategy(Object... arg) {
        if (mock) {
            StrategyMock strategyMock = new StrategyMock(0f);
            strategyMock.add(new StrategyMockResponse(HttpURLConnection.HTTP_OK, "{\"createdAt\":\"2015-08-05T11:14:45.374Z\",\"id\":\"1\",\"name\":\"S3\",\"resolution\":\"720x1280\",\"updatedAt\":\"2015-08-05T11:14:45.374Z\"}"));
            strategyMock.add(new StrategyMockResponse(HttpURLConnection.HTTP_OK, "{\"createdAt\":\"2015-08-05T11:14:45.374Z\",\"id\":\"" + mId + "\",\"name\":\"S3\",\"resolution\":\"720x1280\",\"updatedAt\":\"2015-08-05T11:14:45.374Z\"}"));
            return strategyMock;
        }

        StrategyIonSingleStringGet strategy = new StrategyIonSingleStringGet(String.format(mUrl, mId));
        return strategy;
    }

    @Override
    public Boolean analyzeResult(int aHttpCode, String result) {
        switch (aHttpCode) {
            case HttpURLConnection.HTTP_OK:
                this.mDevice = OperationHelper.getModelObject(result, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Device.class);
                return true;
        }
        return false;
    }

    @Override
    protected void addExtrasForResultOk(Intent intent) {
        intent.putExtra(GetDeviceReceiver.EXTRA_DEVICE, mDevice);
    }

    public interface IGetDeviceReceiver {
        void onNoInternet();

        void onStartOperation();

        void onSuccess(Device aDevice);

        void onError();
    }

    public static class GetDeviceReceiver extends OperationBroadcastReceiver {
        static final String EXTRA_DEVICE = "EXTRA_DEVICE";
        private final IGetDeviceReceiver mCallback;

        public GetDeviceReceiver(IGetDeviceReceiver callback) {
            super();
            mCallback = callback;
        }

        @Override
        protected void onNoInternet() {
            mCallback.onStartOperation();
        }

        @Override
        protected void onStartOperation() {
            mCallback.onStartOperation();
        }

        protected void onResultOK(Intent anIntent) {
            Device device = (Device) anIntent.getSerializableExtra(EXTRA_DEVICE);
            if (device != null)
                mCallback.onSuccess(device);
            else
                mCallback.onError();
        }

        protected void onResultError(Intent anIntent) {
            mCallback.onError();
        }
    }
}