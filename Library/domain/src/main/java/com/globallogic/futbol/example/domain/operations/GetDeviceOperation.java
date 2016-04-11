package com.globallogic.futbol.example.domain.operations;

import android.content.Intent;

import com.globallogic.futbol.core.interfaces.callbacks.IStrategyHttpCallback;
import com.globallogic.futbol.core.broadcasts.OperationHttpBroadcastReceiver;
import com.globallogic.futbol.core.strategies.OperationStrategy;
import com.globallogic.futbol.core.strategies.mock.StrategyHttpMock;
import com.globallogic.futbol.example.data.analyzers.GetDeviceHttpAnalyzer;
import com.globallogic.futbol.example.data.entities.DeviceEntity;
import com.globallogic.futbol.example.data.strategies.GetDeviceFromHttp;
import com.globallogic.futbol.example.domain.models.Device;

import java.util.ArrayList;

/**
 * Created by Facundo Mengoni on 8/6/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class GetDeviceOperation extends BaseOperation {
    private static final String TAG = GetDeviceOperation.class.getSimpleName();

    public void execute(Integer anId) {
        performOperation(anId);
    }

    @Override
    protected ArrayList<OperationStrategy> getStrategies(Object... arg) {
        Integer id = (Integer) arg[0];

        StrategyHttpMock strategyHttpMock = new GetDeviceFromHttp(this, id);
        ArrayList<OperationStrategy> operationStrategies = new ArrayList<>();
        operationStrategies.add(strategyHttpMock);
        return operationStrategies;
    }

    public interface IGetDeviceReceiver extends IStrategyHttpCallback {
        void onSuccess(Device aDevice);

        void onError();
    }

    public static class GetDeviceReceiver extends OperationHttpBroadcastReceiver {
        private final IGetDeviceReceiver mCallback;

        public GetDeviceReceiver(IGetDeviceReceiver callback) {
            super(callback);
            mCallback = callback;
        }

        protected void onResultOK(Intent anIntent) {
            DeviceEntity deviceEntity = (DeviceEntity) anIntent.getSerializableExtra(GetDeviceHttpAnalyzer.EXTRA_DEVICE);
            //ToDo change to mapper
            Device device = Device.fromDeviceEntity(deviceEntity);
            if (device != null) {
                mCallback.onSuccess(device);
            } else {
                mCallback.onError();
            }
        }

        protected void onResultError(Intent anIntent) {
            mCallback.onError();
        }
    }
}