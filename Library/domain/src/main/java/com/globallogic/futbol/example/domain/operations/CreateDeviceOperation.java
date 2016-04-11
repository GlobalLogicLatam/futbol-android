package com.globallogic.futbol.example.domain.operations;

import android.content.Intent;

import com.globallogic.futbol.core.OperationApp;
import com.globallogic.futbol.core.broadcasts.OperationHttpBroadcastReceiver;
import com.globallogic.futbol.core.interfaces.analyzers.IStrategyHttpAnalyzer;
import com.globallogic.futbol.core.interfaces.callbacks.IStrategyHttpCallback;
import com.globallogic.futbol.core.operations.OperationHelper;
import com.globallogic.futbol.core.strategies.OperationStrategy;
import com.globallogic.futbol.core.strategies.mock.StrategyHttpMock;
import com.globallogic.futbol.core.responses.StrategyHttpResponse;
import com.globallogic.futbol.example.domain.models.Device;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Facundo Mengoni on 8/6/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class CreateDeviceOperation extends BaseOperation {
    private static final String TAG = CreateDeviceOperation.class.getSimpleName();

    public void execute(String name, String resolution) {
        performOperation(name, resolution);
    }

    @Override
    protected ArrayList<OperationStrategy> getStrategies(Object... arg) {
        String name = (String) arg[0];
        String resolution = (String) arg[1];

        StrategyHttpMock strategyHttpMock = new StrategyHttpMock(this, new IStrategyHttpAnalyzer() {
            private Device mDevice;

            @Override
            public void analyzeException(Exception anException) {
                CreateDeviceOperation.this.analyzeException(anException);
            }

            @Override
            public Boolean analyzeResult(Integer aHttpCode, String aString) {
                switch (aHttpCode) {
                    case HttpURLConnection.HTTP_CREATED:
                        this.mDevice = OperationHelper.getModelObject(aString, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Device.class);
                        return true;
                }
                return false;
            }

            @Override
            public void addExtrasForResultError(Intent intent) {
                CreateDeviceOperation.this.addExtrasForResultError(intent);
            }

            @Override
            public void addExtrasForResultOk(Intent intent) {
                intent.putExtra(CreateDeviceReceiver.EXTRA_DEVICE, mDevice);
            }
        }, 0f);
        try {
            strategyHttpMock.add(new StrategyHttpResponse(HttpURLConnection.HTTP_CREATED, String.format(OperationHelper.assetsReader(OperationApp.getInstance(), "json/CreateDeviceOperation_1.json"), name, resolution)));
        } catch (IOException ignored) {
        }
        return new ArrayList<OperationStrategy>(Collections.singletonList(strategyHttpMock));
    }

    public interface ICreateDeviceReceiver extends IStrategyHttpCallback {
        void onSuccess(Device aDevice);

        void onError();
    }

    public static class CreateDeviceReceiver extends OperationHttpBroadcastReceiver {
        static final String EXTRA_DEVICE = "EXTRA_DEVICE";
        private final ICreateDeviceReceiver mCallback;

        public CreateDeviceReceiver(ICreateDeviceReceiver callback) {
            super(callback);
            mCallback = callback;
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