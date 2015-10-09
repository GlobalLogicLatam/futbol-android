package com.globallogic.futbol.example.operations;

import android.content.Intent;
import android.os.Bundle;

import com.globallogic.futbol.core.OperationApp;
import com.globallogic.futbol.core.OperationResponse;
import com.globallogic.futbol.core.interfaces.IOperationStrategy;
import com.globallogic.futbol.core.operation.OperationBroadcastReceiver;
import com.globallogic.futbol.core.operation.OperationHelper;
import com.globallogic.futbol.core.operation.strategies.StrategyHttpMock;
import com.globallogic.futbol.core.operation.strategies.StrategyHttpMockResponse;
import com.globallogic.futbol.example.entities.Device;
import com.globallogic.futbol.example.operations.helper.ExampleOperationHttp;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by Facundo Mengoni on 8/6/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class GetDeviceOperationHttp extends ExampleOperationHttp {
    private static final String TAG = GetDeviceOperationHttp.class.getSimpleName();
    private static final String SAVE_INSTANCE_DEVICE = "SAVE_INSTANCE_DEVICE";

    private final Integer mId;

    private Device mDevice;

    public GetDeviceOperationHttp(Integer anId) {
        super(anId.toString());
        mId = anId;
    }

    @Override
    public void reset() {
        super.reset();
        mDevice = null;
    }

    public void execute() {
        performOperation();
    }

    @Override
    protected IOperationStrategy getStrategy(Object... arg) {
        StrategyHttpMock strategyHttpMock = new StrategyHttpMock(0f);
        try {
            String name = "";
            switch (mId) {
                case 1:
                    name = "S3";
                    break;
                case 2:
                    name = "Moto G";
                    break;
            }
            strategyHttpMock.add(new StrategyHttpMockResponse(HttpURLConnection.HTTP_OK, String.format(OperationHelper.assetsReader(OperationApp.getInstance(), "json/GetDeviceOperation_1.json"), mId, name)));
        } catch (IOException e) {
        }
        return strategyHttpMock;
    }

    @Override
    public Boolean analyzeResult(OperationResponse<String> response) {
        switch (response.getResultCode()) {
            case HttpURLConnection.HTTP_OK:
                this.mDevice = OperationHelper.getModelObject(response.getResult(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Device.class);
                return true;
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mDevice != null)
            outState.putSerializable(SAVE_INSTANCE_DEVICE, mDevice);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(SAVE_INSTANCE_DEVICE))
            mDevice = (Device) savedInstanceState.getSerializable(SAVE_INSTANCE_DEVICE);
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