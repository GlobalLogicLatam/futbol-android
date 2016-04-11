package com.globallogic.futbol.example.domain.operations;

import android.content.Intent;

import com.globallogic.futbol.core.analyzers.DefaultAnalyzer;
import com.globallogic.futbol.core.broadcasts.OperationHttpBroadcastReceiver;
import com.globallogic.futbol.core.interfaces.callbacks.IStrategyHttpCallback;
import com.globallogic.futbol.core.strategies.OperationStrategy;
import com.globallogic.futbol.core.strategies.mock.StrategyDbMock;
import com.globallogic.futbol.core.strategies.mock.StrategyHttpMock;
import com.globallogic.futbol.example.data.analyzers.GetDevicesHttpAnalyzer;
import com.globallogic.futbol.example.data.entities.DeviceEntity;
import com.globallogic.futbol.example.data.strategies.GetDevicesFromDb;
import com.globallogic.futbol.example.data.strategies.GetDevicesFromHttp;
import com.globallogic.futbol.example.domain.models.Device;

import java.util.ArrayList;

/**
 * Created by Facundo Mengoni on 8/6/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class GetDevicesOperation extends BaseOperation {
    private static final String TAG = GetDevicesOperation.class.getSimpleName();
    private boolean callHttp;
    private boolean callDB;

    public GetDevicesOperation() {
        super();
    }

    public void execute() {
        callHttp = true;
        callDB = true;
        performOperation();
    }

    public void executeOnlyDB() {
        callHttp = false;
        callDB = true;
        performOperation();
    }

    public void executeOnlyHttp() {
        callHttp = true;
        callDB = false;
        performOperation();
    }

    @Override
    protected ArrayList<OperationStrategy> getStrategies(Object... arg) {
        StrategyHttpMock strategyHttpMock = new GetDevicesFromHttp(this);
        StrategyDbMock strategyDbMock = new GetDevicesFromDb(this);
        ArrayList<OperationStrategy> operationStrategies = new ArrayList<>();
        if (callDB)
            operationStrategies.add(strategyDbMock);
        if (callHttp)
            operationStrategies.add(strategyHttpMock);
        return operationStrategies;
    }

    public interface IGetDevicesReceiver extends IStrategyHttpCallback {
        void onSuccessHttp(ArrayList<Device> aDevice);

        void onSuccessDb(ArrayList<Device> aDevice);

        void onError();
    }

    public static class GetDevicesReceiver extends OperationHttpBroadcastReceiver {
        private final IGetDevicesReceiver mCallback;

        public GetDevicesReceiver(IGetDevicesReceiver callback) {
            super(callback);
            mCallback = callback;
        }

        protected void onResultOK(Intent anIntent) {
            //noinspection unchecked
            ArrayList<DeviceEntity> listEntities = (ArrayList<DeviceEntity>) anIntent.getSerializableExtra(DefaultAnalyzer.EXTRA_RESULT);
            ArrayList<Device> list = new ArrayList<>();
            for (DeviceEntity deviceEntity : listEntities) {
                //ToDo change to mapper
                list.add(Device.fromDeviceEntity(deviceEntity));
            }
            if (GetDevicesHttpAnalyzer.TYPE_HTTP.equals(anIntent.getStringExtra(GetDevicesHttpAnalyzer.EXTRA_TYPE)))
                mCallback.onSuccessHttp(list);
            else
                mCallback.onSuccessDb(list);
        }

        protected void onResultError(Intent anIntent) {
            mCallback.onError();
        }
    }
}