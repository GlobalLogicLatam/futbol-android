package com.globallogic.futbol.core.operation.strategies;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.globallogic.futbol.core.OperationApp;
import com.globallogic.futbol.core.interfaces.IOperationStrategy;
import com.globallogic.futbol.core.interfaces.IStrategyCallback;
import com.globallogic.futbol.core.operation.database.OperationDatabaseHelper;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * Created by Agustin Larghi on 13/10/2015.
 * Globallogic
 * agustin.larghi@globallogic.com
 */
public class StrategySqliteMock implements IOperationStrategy {

    public static final String DETAIL_MESSAGE = "Hardcode dummy exception";
    private static final String TAG = StrategySqliteMock.class.getSimpleName();
    protected final ArrayList<StrategySqliteMockResponse> responses = new ArrayList<StrategySqliteMockResponse>();
    protected final ArrayList<Exception> responsesException = new ArrayList<Exception>();
    private final Float mErrorProbability;
    private Random random = new Random();
    private IStrategyCallback mCallback;

    /**
     * A StrategyHttpMock allows to simulate any response that the server can return.
     *
     * @param errorProbability Determines the probability that the error occurs. If it is less than or equal to 0 returns a {@link StrategyHttpMockResponse} (or an exception if there is no added replies). If it is greater or equal to 1 always returns an exception
     */
    public StrategySqliteMock(float errorProbability) {
        this.mErrorProbability = errorProbability;
    }

    @Override
    public void updateCallback(IStrategyCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void doRequest(IStrategyCallback callback) {
        this.mCallback = callback;
        if (random.nextFloat() < mErrorProbability) {
            // Ejecuto un error
            Exception mockException = getMockException();
            if (mockException != null)
                mCallback.parseResponse(mockException, new SqliteOperationResponse("", null));
            else
                onNotResponseAdded();
        } else {
            // Retorno alguna respuesta dummy
            StrategySqliteMockResponse mockResponse = getMockResponse();
            if (mockResponse != null) {
                OperationDatabaseHelper mHelper = new OperationDatabaseHelper(OperationApp.getInstance(), mockResponse.getAssetsDatabasePath());
                try {
                    mHelper.createDataBase();
                    mHelper.openDataBase();
                    mHelper.close();
                    SQLiteDatabase readableDatabase = mHelper.getReadableDatabase();
                    Cursor resultCursor = readableDatabase.rawQuery(mockResponse.getQuery(), null);
                    mCallback.parseResponse(null, new SqliteOperationResponse(mockResponse.getQuery(), resultCursor));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                onNotResponseAdded();
            }
        }
    }


    private void onNotResponseAdded() {
        mCallback.parseResponse(new Exception(DETAIL_MESSAGE), new FileOperationResponse(0, null));
    }

    private StrategySqliteMockResponse getMockResponse() {
        if (responses.size() > 0) {
            return responses.get(random.nextInt(responses.size()));
        }
        return null;
    }

    private Exception getMockException() {
        if (responsesException.size() > 0) {
            return responsesException.get(random.nextInt(responsesException.size()));
        }
        return null;
    }

    /**
     * Adds an expected response
     *
     * @see StrategyHttpMockResponse
     */
    public StrategySqliteMock add(StrategySqliteMockResponse mockResponse) {
        responses.add(mockResponse);
        return this;
    }

    /**
     * Adds an expected exception
     */
    public StrategySqliteMock add(Exception exception) {
        responsesException.add(exception);
        return this;
    }

    public StrategySqliteMock addJsonSyntaxException() {
        responsesException.add(new JsonSyntaxException(DETAIL_MESSAGE));
        return this;
    }

    public StrategySqliteMock addSocketException() {
        responsesException.add(new SocketException(DETAIL_MESSAGE));
        return this;
    }

    public StrategySqliteMock addMalformedURLException() {
        responsesException.add(new MalformedURLException(DETAIL_MESSAGE));
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

    public StrategySqliteMock addException() {
        responsesException.add(new Exception(DETAIL_MESSAGE));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StrategyFileMock)) return false;
        StrategyFileMock that = (StrategyFileMock) o;
        return responses.equals(that.responses);
    }

    @Override
    public int hashCode() {
        return responses.hashCode();
    }

}
