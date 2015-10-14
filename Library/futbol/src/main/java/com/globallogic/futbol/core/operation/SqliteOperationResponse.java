package com.globallogic.futbol.core.operation;

import android.database.Cursor;

import com.globallogic.futbol.core.OperationResponse;

/**
 * Created by Agustin Larghi on 07/10/2015.
 * Globallogic
 * agustin.larghi@globallogic.com
 */
public class SqliteOperationResponse extends OperationResponse<String, Cursor> {
    public SqliteOperationResponse(Cursor aCursor) {
        super(null, aCursor);
    }
}
