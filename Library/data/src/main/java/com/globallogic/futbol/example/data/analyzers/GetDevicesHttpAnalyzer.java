package com.globallogic.futbol.example.data.analyzers;

import android.content.Intent;

import com.globallogic.futbol.core.analyzers.DefaultHttpAnalyzer;
import com.globallogic.futbol.core.interfaces.analyzers.IStrategyHttpAnalyzer;
import com.globallogic.futbol.core.operations.OperationHelper;
import com.globallogic.futbol.example.data.entities.DeviceEntity;

import java.util.ArrayList;

/**
 * @author facundo.mengoni
 * @since 0.3
 */
public class GetDevicesHttpAnalyzer extends DefaultHttpAnalyzer<ArrayList<DeviceEntity>> implements IStrategyHttpAnalyzer {
    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final String TYPE_HTTP = "TYPE_HTTP";

    @Override
    protected ArrayList<DeviceEntity> parseResponse(String result) {
        return OperationHelper.getModelArray(result, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", DeviceEntity.class);
    }

    @Override
    public void addExtrasForResultOk(Intent intent) {
        super.addExtrasForResultOk(intent);
        intent.putExtra(EXTRA_TYPE, TYPE_HTTP);
    }
}