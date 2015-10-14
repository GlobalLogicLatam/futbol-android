package com.globallogic.futbol.example.operations;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.globallogic.futbol.core.OperationResponse;
import com.globallogic.futbol.core.interfaces.IOperationStrategy;
import com.globallogic.futbol.core.operation.OperationBroadcastReceiver;
import com.globallogic.futbol.core.operation.strategies.StrategySqliteMock;
import com.globallogic.futbol.core.operation.strategies.StrategySqliteMockResponse;
import com.globallogic.futbol.example.entities.Device;
import com.globallogic.futbol.example.operations.helper.ExampleOperationSqlite;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Agustin Larghi on 08/10/2015.
 * Globallogic
 * agustin.larghi@globallogic.com
 */
public class SelectDevicesOperation extends ExampleOperationSqlite {

    private static final String TAG = SelectDevicesOperation.class.getSimpleName();
    private static final String SAVE_INSTANCE_FILE = "SAVE_INSTANCE_FILE";

    private ArrayList<Device> mDevices = new ArrayList<>();

    public SelectDevicesOperation() {
        super("");
    }

    @Override
    public void reset() {
        super.reset();
    }

    public void execute() {
        performOperation();
    }

    @Override
    protected IOperationStrategy getStrategy(Object... arg) {
        StrategySqliteMock strategyFileMock = new StrategySqliteMock(0f);
        strategyFileMock.add(new StrategySqliteMockResponse("devices.db", "select * from DEVICE;"));
        return strategyFileMock;
    }

    @Override
    public Boolean analyzeResult(OperationResponse<String, Cursor> result) {
        Cursor cursor = result.getResult();
        for (int idx = 0; idx < cursor.getCount(); idx++) {
            cursor.moveToPosition(idx);
            Device dbDevice = new Device();
            dbDevice.setCreatedAt(new Date(cursor.getLong(cursor.getColumnIndex("createdAt"))));
            dbDevice.setCreatedAt(new Date(cursor.getLong(cursor.getColumnIndex("updatedAt"))));
            dbDevice.setName(cursor.getString(cursor.getColumnIndex("name")));
            dbDevice.setResolution(cursor.getString(cursor.getColumnIndex("resolution")));
            dbDevice.setId(cursor.getInt(cursor.getColumnIndex("id")));
            mDevices.add(dbDevice);
        }
        return true;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mDevices != null)
            outState.putSerializable(SAVE_INSTANCE_FILE, mDevices);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(SAVE_INSTANCE_FILE))
            mDevices = (ArrayList<Device>) savedInstanceState.getSerializable(SAVE_INSTANCE_FILE);
    }

    @Override
    protected void addExtrasForResultOk(Intent intent) {
        intent.putExtra(SelectDevicesReceiver.EXTRA_DEVICE, mDevices);
    }

    public interface ISelectDevicesCallback {
        void onNoInternet();

        void onStartOperation();

        void onSuccess(ArrayList<Device> dbDevices);

        void onError();

        void onFinishOperation();
    }

    public static class SelectDevicesReceiver extends OperationBroadcastReceiver {
        static final String EXTRA_DEVICE = "EXTRA_DEVICE";
        private final ISelectDevicesCallback mCallback;

        public SelectDevicesReceiver(ISelectDevicesCallback callback) {
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
            ArrayList<Device> dbDevices = (ArrayList<Device>) anIntent.getSerializableExtra(EXTRA_DEVICE);
            if (dbDevices != null)
                mCallback.onSuccess(dbDevices);
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
