package com.globallogic.futbol.core.operation.strategies;

import android.os.Handler;

import com.globallogic.futbol.core.interfaces.IOperationStrategy;
import com.globallogic.futbol.core.interfaces.IStrategyCallback;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class StrategyMock implements IOperationStrategy {
    public static final String DETAIL_MESSAGE = "Hardcode dummy exception";
    private static final String TAG = StrategyMock.class.getSimpleName();
    protected final ArrayList<StrategyMockResponse> responses = new ArrayList<StrategyMockResponse>();
    protected final ArrayList<Exception> responsesException = new ArrayList<Exception>();
    private final Float mErrorProbability;
    private Random random = new Random();
    private IStrategyCallback mCallback;

    /**
     * A StrategyMock allows to simulate any response that the server can return.
     *
     * @param errorProbability Determines the probability that the error occurs. If it is less than or equal to 0 returns a {@link StrategyMockResponse} (or an exception if there is no added replies). If it is greater or equal to 1 always returns an exception
     */
    public StrategyMock(float errorProbability) {
        this.mErrorProbability = errorProbability;
    }

    @Override
    public void updateCallback(IStrategyCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void doRequest(IStrategyCallback callback) {
        this.mCallback = callback;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (random.nextFloat() < mErrorProbability) {
                    // Ejecuto un error
                    Exception mockException = getMockException();
                    if (mockException != null)
                        mCallback.parseResponse(mockException, 0, null);
                    else
                        onNotResponseAdded();
                } else {
                    // Retorno alguna respuesta dummy
                    StrategyMockResponse mockResponse = getMockResponse();
                    if (mockResponse != null)
                        mCallback.parseResponse(null, mockResponse.getHttpCode(), mockResponse.getResponse());
                    else
                        onNotResponseAdded();
                }
            }
        }, 100);
    }

    private void onNotResponseAdded() {
        mCallback.parseResponse(new Exception(DETAIL_MESSAGE), 0, null);
    }

    private StrategyMockResponse getMockResponse() {
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
     * @see StrategyMockResponse
     */
    public StrategyMock add(StrategyMockResponse mockResponse) {
        responses.add(mockResponse);
        return this;
    }

    /**
     * Adds an expected exception
     */
    public StrategyMock add(Exception exception) {
        responsesException.add(exception);
        return this;
    }

    public StrategyMock addJsonSyntaxException() {
        responsesException.add(new JsonSyntaxException(DETAIL_MESSAGE));
        return this;
    }

    public StrategyMock addSocketException() {
        responsesException.add(new SocketException(DETAIL_MESSAGE));
        return this;
    }

    public StrategyMock addMalformedURLException() {
        responsesException.add(new MalformedURLException(DETAIL_MESSAGE));
        return this;
    }

    public StrategyMock addTimeoutException() {
        responsesException.add(new TimeoutException(DETAIL_MESSAGE));
        return this;
    }

    public StrategyMock addIOException() {
        responsesException.add(new IOException(DETAIL_MESSAGE));
        return this;
    }

    public StrategyMock addException() {
        responsesException.add(new Exception(DETAIL_MESSAGE));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StrategyMock)) return false;

        StrategyMock that = (StrategyMock) o;

        return responses.equals(that.responses);
    }

    @Override
    public int hashCode() {
        return responses.hashCode();
    }
}