package com.globallogic.futbol.example.operations;

import android.content.Intent;

import com.globallogic.futbol.core.OperationApp;
import com.globallogic.futbol.core.interfaces.IOperationStrategy;
import com.globallogic.futbol.core.operation.OperationBroadcastReceiver;
import com.globallogic.futbol.core.operation.OperationHelper;
import com.globallogic.futbol.core.operation.strategies.StrategyMock;
import com.globallogic.futbol.core.operation.strategies.StrategyMockResponse;
import com.globallogic.futbol.example.entities.Device;
import com.globallogic.futbol.example.operations.helper.ExampleOperation;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by Facundo Mengoni on 8/6/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class GetDeviceOperation extends ExampleOperation {
    private static final String TAG = GetDeviceOperation.class.getSimpleName();

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

    @Override
    protected IOperationStrategy getStrategy(Object... arg) {
        StrategyMock strategyMock = new StrategyMock(0f);
        try {
            strategyMock.add(new StrategyMockResponse(HttpURLConnection.HTTP_OK, String.format(OperationHelper.assetsReader(OperationApp.getInstance(), "json/DeleteDeviceOperation_1.json"), mId)));
        } catch (IOException e) {
        }
        return strategyMock;
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

        void onFinishOperation();
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
            mCallback.onNoInternet();
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

        @Override
        protected void onFinishOperation() {
            mCallback.onFinishOperation();
        }
    }
}