package com.globallogic.futbol.example.data.analyzers;

import android.content.Intent;

import com.globallogic.futbol.core.analyzers.DefaultDBAnalyzer;
import com.globallogic.futbol.example.data.entities.DeviceEntity;

import java.util.ArrayList;

/**
 * @author facundo.mengoni
 * @since 0.3
 */
public class GetDevicesDbAnalyzer extends DefaultDBAnalyzer<ArrayList<DeviceEntity>> {
    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final String TYPE_DB = "TYPE_DB";

    @Override
    public void addExtrasForResultOk(Intent intent) {
        super.addExtrasForResultOk(intent);
        intent.putExtra(EXTRA_TYPE, TYPE_DB);
    }
}