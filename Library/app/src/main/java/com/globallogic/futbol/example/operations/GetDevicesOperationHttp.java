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
import java.util.ArrayList;

/**
 * Created by Facundo Mengoni on 8/6/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class GetDevicesOperationHttp extends ExampleOperationHttp {
    private static final String TAG = GetDevicesOperationHttp.class.getSimpleName();
    private static final String SAVE_INSTANCE_DEVICES = "SAVE_INSTANCE_DEVICES";

    private ArrayList<Device> mList;

    public GetDevicesOperationHttp(String anId) {
        super(anId);
    }

    @Override
    public void reset() {
        super.reset();
        mList = null;
    }

    public void execute() {
        performOperation();
    }

    @Override
    protected IOperationStrategy getStrategy(Object... arg) {
        StrategyHttpMock strategyHttpMock = new StrategyHttpMock(0f);
        try {
            strategyHttpMock.add(new StrategyHttpMockResponse(HttpURLConnection.HTTP_OK, OperationHelper.assetsReader(OperationApp.getInstance(), "json/GetDevicesOperation_1.json")));
        } catch (IOException e) {
        }
        return strategyHttpMock;
    }

    @Override
    public Boolean analyzeResult(OperationResponse<String> response) {
        switch (response.getResultCode()) {
            case HttpURLConnection.HTTP_OK:
                this.mList = OperationHelper.getModelArray(response.getResult(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Device.class);
                return true;
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mList != null)
            outState.putSerializable(SAVE_INSTANCE_DEVICES, mList);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(SAVE_INSTANCE_DEVICES))
            mList = (ArrayList<Device>) savedInstanceState.getSerializable(SAVE_INSTANCE_DEVICES);
    }

    @Override
    protected void addExtrasForResultOk(Intent intent) {
        intent.putExtra(GetDevicesReceiver.EXTRA_DEVICES, mList);
    }

    public ArrayList<Device> getDevices() {
        if (mList == null)
            new ArrayList<>();
        return new ArrayList<>(mList);
    }

    public interface IGetDevicesReceiver {
        void onNoInternet();

        void onStartOperation();

        void onSuccess(ArrayList<Device> aDevice);

        void onError();

        void onFinishOperation();
    }

    public static class GetDevicesReceiver extends OperationBroadcastReceiver {
        static final String EXTRA_DEVICES = "EXTRA_DEVICES";
        private final IGetDevicesReceiver mCallback;

        public GetDevicesReceiver(IGetDevicesReceiver callback) {
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
            ArrayList<Device> list = (ArrayList<Device>) anIntent.getSerializableExtra(EXTRA_DEVICES);
            if (list != null)
                mCallback.onSuccess(list);
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