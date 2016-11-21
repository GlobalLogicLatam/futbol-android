package com.globallogic.futbol.example.data.analyzers;

import android.content.Intent;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategyHttpAnalyzer;
import com.globallogic.futbol.core.operations.OperationHelper;
import com.globallogic.futbol.example.data.entities.DeviceEntity;

import java.net.HttpURLConnection;

/**
 * @author facundo.mengoni
 * @since 0.3
 */
public class GetDeviceHttpAnalyzer implements IStrategyHttpAnalyzer {
    public static final String EXTRA_DEVICE = "EXTRA_DEVICE";

    protected DeviceEntity mDevice;

    public void analyzeException(Exception anException) {
        // Manage exception to notify the error
    }

    public Boolean analyzeResult(Integer aHttpCode, String result) {
        switch (aHttpCode) {
            case HttpURLConnection.HTTP_OK:
                this.mDevice = OperationHelper.getModelObject(result, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", DeviceEntity.class);
                return true;
        }
        return false;
    }

    public Boolean analyzeResult(DeviceEntity aResponse) {
        this.mDevice = aResponse;
        return true;
    }

    @Override
    public void reset() {
        mDevice = null;
    }

    public void addExtrasForResultError(Intent intent) {
        // Notify the error
    }

    public void addExtrasForResultOk(Intent intent) {
        intent.putExtra(EXTRA_DEVICE, mDevice);
    }
}