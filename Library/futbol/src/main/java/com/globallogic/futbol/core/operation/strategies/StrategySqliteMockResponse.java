package com.globallogic.futbol.core.operation.strategies;

import android.database.Cursor;

/**
 * Created by Agustin Larghi on 13/10/2015.
 * Globallogic
 * agustin.larghi@globallogic.com
 */
public class StrategySqliteMockResponse extends StrategyMockResponse<Cursor> {

    private String query;

    public StrategySqliteMockResponse(String assetFilename, String sqliQuery) {
        setAssetsDatabasePath(assetFilename);
        setQuery(sqliQuery);
    }

    public String getAssetsDatabasePath() {
        return getStatus();
    }

    public void setAssetsDatabasePath(String assetsDatabasePath) {
        setStatus(assetsDatabasePath);
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
