package com.globallogic.futbol.core.operation.strategies;

import com.globallogic.futbol.core.operation.HttpOperationResponse;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.concurrent.TimeoutException;

public class StrategyHttpMock extends StrategyMock<StrategyHttpMockResponse> {

    public StrategyHttpMock(float errorProbability) {
        super(errorProbability);
    }

    @Override
    protected void executeSuccessfulMockRequest() {
        // Retorno alguna respuesta dummy
        StrategyHttpMockResponse mockResponse = (StrategyHttpMockResponse) getMockResponse();
        if (mockResponse != null)
            mCallback.parseResponse(null, new HttpOperationResponse(mockResponse.getHttpCode(), mockResponse.getResponse()));
        else
            onNotResponseAdded();
    }

    @Override
    protected void executeFailedMockRequest() {
        // Ejecuto un error
        Exception mockException = getMockException();
        if (mockException != null)
            mCallback.parseResponse(mockException, new HttpOperationResponse(0, null));
        else
            onNotResponseAdded();
    }

    private void onNotResponseAdded() {
        mCallback.parseResponse(new Exception(DETAIL_MESSAGE), new HttpOperationResponse(0, null));
    }

    /**
     * Adds an expected exception
     */

    public StrategyHttpMock addJsonSyntaxException() {
        responsesException.add(new JsonSyntaxException(DETAIL_MESSAGE));
        return this;
    }

    public StrategyHttpMock addSocketException() {
        responsesException.add(new SocketException(DETAIL_MESSAGE));
        return this;
    }

    public StrategyHttpMock addMalformedURLException() {
        responsesException.add(new MalformedURLException(DETAIL_MESSAGE));
        return this;
    }

    public StrategyHttpMock addTimeoutException() {
        responsesException.add(new TimeoutException(DETAIL_MESSAGE));
        return this;
    }

    public StrategyHttpMock addIOException() {
        responsesException.add(new IOException(DETAIL_MESSAGE));
        return this;
    }

}