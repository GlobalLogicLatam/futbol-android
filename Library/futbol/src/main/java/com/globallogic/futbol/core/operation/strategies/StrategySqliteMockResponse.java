package com.globallogic.futbol.core.operation.strategies;

import java.io.Serializable;

/**
 * Created by Agustin Larghi on 13/10/2015.
 * Globallogic
 * agustin.larghi@globallogic.com
 */
public class StrategySqliteMockResponse implements Serializable {
    private final String mAssetsDatabasePath;
    private final String mQuery;
    private int resultCode;

    public StrategySqliteMockResponse(String databasePath, String query) {
        mAssetsDatabasePath = databasePath;
        mQuery = query;
    }

    public String getAssetsDatabasePath() {
        return mAssetsDatabasePath;
    }

    public String getQuery() {
        return mQuery;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
