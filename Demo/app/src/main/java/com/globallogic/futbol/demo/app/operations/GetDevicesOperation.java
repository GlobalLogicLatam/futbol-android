package com.globallogic.futbol.demo.app.operations;

import android.content.Context;
import android.content.Intent;

import com.globallogic.futbol.core.analyzers.DefaultHttpAnalyzer;
import com.globallogic.futbol.core.broadcasts.OperationBroadcastReceiver;
import com.globallogic.futbol.core.interfaces.analyzers.IStrategyHttpAnalyzer;
import com.globallogic.futbol.core.interfaces.callbacks.IStrategyHttpCallback;
import com.globallogic.futbol.core.operations.OperationHelper;
import com.globallogic.futbol.core.responses.StrategyHttpResponse;
import com.globallogic.futbol.core.strategies.OperationStrategy;
import com.globallogic.futbol.core.strategies.mock.StrategyHttpMock;
import com.globallogic.futbol.demo.R;
import com.globallogic.futbol.demo.app.analyzers.MyDefaultHttpAnalyzer;
import com.globallogic.futbol.demo.app.interfaces.MyStrategyHttpCallback;
import com.globallogic.futbol.demo.app.receivers.MyReceiver;
import com.globallogic.futbol.demo.domain.Device;
import com.globallogic.futbol.strategies.ion.StrategyIonSingleStringGet;

import java.io.Serializable;
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

    private boolean mock = false;

    @Override
    protected ArrayList<OperationStrategy> getStrategies(Object... args) {
        ArrayList<OperationStrategy> strategies = new ArrayList<>();
        MyDefaultHttpAnalyzer analyzer = new MyDefaultHttpAnalyzer() {
            private ArrayList<Device> mList;

            @Override
            public Boolean analyzeResult(Integer aHttpCode, String aString) {
                switch (aHttpCode) {
                    case HttpURLConnection.HTTP_OK:
                        this.mList = OperationHelper.getModelArray(aString, Device.class);
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
                intent.putExtra(GetDevicesReceiver.DEVICES, mList);
            }
        };
        if (mock) {
            StrategyHttpMock strategy = new StrategyHttpMock(this, analyzer, 0f);
            strategy.add(new StrategyHttpResponse(200, "[{\"id\":1,\"name\":\"S3\",\"resolution\":\"720x1280\",\"createdAt\":\"2015-08-06T13:19:39.000Z\",\"updatedAt\":\"2015-08-06T13:21:03.000Z\"}]"));
            strategies.add(strategy);
        } else {
            StrategyIonSingleStringGet strategy = new StrategyIonSingleStringGet(this, analyzer, URL);
            strategies.add(strategy);
        }
        return strategies;
    }

    public interface IGetDevicesOperation extends MyStrategyHttpCallback {
        void onSuccess(ArrayList<Device> devices);
    }

    public static class GetDevicesReceiver extends MyReceiver {
        public static final String DEVICES = "DEVICES";

        private final IGetDevicesOperation mCallback;

        public GetDevicesReceiver(IGetDevicesOperation mCallback) {
            super(mCallback);
            this.mCallback = mCallback;
        }

        @Override
        protected void onResultOK(Intent intent) {
            ArrayList<Device> devices = (ArrayList<Device>) intent.getSerializableExtra(DEVICES);
            if (devices != null)
                mCallback.onSuccess(devices);
            else
                onResultError(intent);
        }
    }
}