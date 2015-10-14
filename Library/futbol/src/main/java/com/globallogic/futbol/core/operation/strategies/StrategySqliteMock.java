package com.globallogic.futbol.core.operation.strategies;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.globallogic.futbol.core.OperationApp;
import com.globallogic.futbol.core.operation.FileOperationResponse;
import com.globallogic.futbol.core.operation.SqliteOperationResponse;
import com.globallogic.futbol.core.operation.database.OperationDatabaseHelper;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Agustin Larghi on 13/10/2015.
 * Globallogic
 * agustin.larghi@globallogic.com
 */
public class StrategySqliteMock extends StrategyMock<StrategySqliteMockResponse> {

    public StrategySqliteMock(float errorProbability) {
        super(errorProbability);
    }

    @Override
    protected void executeFailedMockRequest() {
        // Ejecuto un error
        Exception mockException = getMockException();
        if (mockException != null)
            mCallback.parseResponse(mockException, new SqliteOperationResponse(null));
        else
            onNotResponseAdded();
    }

    @Override
    protected void executeSuccessfulMockRequest() {
        // Retorno alguna respuesta dummy
        StrategySqliteMockResponse mockResponse = (StrategySqliteMockResponse) getMockResponse();
        if (mockResponse != null) {
            OperationDatabaseHelper mHelper = new OperationDatabaseHelper(OperationApp.getInstance(), mockResponse.getAssetsDatabasePath());
            try {
                mHelper.createDataBase();
                mHelper.openDataBase();
                mHelper.close();
                SQLiteDatabase readableDatabase = mHelper.getReadableDatabase();
                Cursor resultCursor = readableDatabase.rawQuery(mockResponse.getQuery(), null);
                mCallback.parseResponse(null, new SqliteOperationResponse(resultCursor));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            onNotResponseAdded();
        }
    }


    private void onNotResponseAdded() {
        mCallback.parseResponse(new Exception(DETAIL_MESSAGE), new FileOperationResponse(0, null));
    }

    /**
     * Adds an expected exception
     */
    public StrategySqliteMock addSocketException() {
        responsesException.add(new SocketException(DETAIL_MESSAGE));
        return this;
    }

    public StrategySqliteMock addTimeoutException() {
        responsesException.add(new TimeoutException(DETAIL_MESSAGE));
        return this;
    }

    public StrategySqliteMock addIOException() {
        responsesException.add(new IOException(DETAIL_MESSAGE));
        return this;
    }


}
