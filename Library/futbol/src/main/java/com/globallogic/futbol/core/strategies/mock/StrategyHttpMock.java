package com.globallogic.futbol.core.strategies.mock;

import android.os.Handler;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategyHttpAnalyzer;
import com.globallogic.futbol.core.operations.Operation;
import com.globallogic.futbol.core.responses.StrategyHttpResponse;
import com.globallogic.futbol.core.strategies.HttpOperationStrategy;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * It allows mock responses from the server.
 */
public class StrategyHttpMock extends HttpOperationStrategy {
    public static final String DETAIL_MESSAGE = "Hardcode dummy exception";
    protected final ArrayList<StrategyHttpResponse> responses = new ArrayList<>();
    protected final ArrayList<Exception> responsesException = new ArrayList<>();
    private final Float mErrorProbability;
    private Random random = new Random();

    /**
     * A StrategyHttpMock allows to simulate any response that the server can return.
     *
     * @param analyzer         An analyzer for the responses mocked.
     * @param errorProbability Determines the probability that the error occurs. If it is less than or equal to 0 returns a {@link StrategyHttpResponse} (or an exception if there is no added replies). If it is greater or equal to 1 always returns an exception
     */
    public StrategyHttpMock(Operation anOperation, IStrategyHttpAnalyzer analyzer, float errorProbability) {
        super(anOperation, analyzer);
        this.mErrorProbability = errorProbability;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doRequestImpl() {
        if (random.nextFloat() < mErrorProbability) {
            // Ejecuto un error
            Exception mockException = getMockException();
            if (mockException != null)
                parseResponse(mockException, null);
            else
                onNotResponseAdded();
        } else {
            // Retorno alguna respuesta dummy
            StrategyHttpResponse mockResponse = getMockResponse();
            if (mockResponse != null)
                parseResponse(null, mockResponse);
            else
                onNotResponseAdded();
        }
    }

    /**
     * A default response to return when any response was set.
     */
    private void onNotResponseAdded() {
        parseResponse(new Exception(DETAIL_MESSAGE), null);
    }

    /**
     * Obtains a response mocked. It will be some random response.
     *
     * @return Some response previously added.
     */
    private StrategyHttpResponse getMockResponse() {
        if (responses.size() > 0) {
            return responses.get(random.nextInt(responses.size()));
        }
        return null;
    }

    /**
     * Obtains a exception mocked. It will be some random exception.
     *
     * @return Some exception previously added.
     */
    private Exception getMockException() {
        if (responsesException.size() > 0) {
            return responsesException.get(random.nextInt(responsesException.size()));
        }
        return null;
    }

    /**
     * Adds an expected response.
     *
     * @param mockResponse The response to add.
     * @return The strategy where was added.
     * @see StrategyHttpResponse
     */
    public StrategyHttpMock add(StrategyHttpResponse mockResponse) {
        responses.add(mockResponse);
        return this;
    }

    /**
     * Adds an expected exception.
     *
     * @param exception The exception to add.
     * @return The strategy where was added.
     */
    public StrategyHttpMock add(Exception exception) {
        responsesException.add(exception);
        return this;
    }

    /**
     * Adds a {@link JsonSyntaxException} exception.
     *
     * @return The strategy where was added.
     * @see #add(Exception)
     */
    public StrategyHttpMock addJsonSyntaxException() {
        responsesException.add(new JsonSyntaxException(DETAIL_MESSAGE));
        return this;
    }

    /**
     * Adds a {@link SocketException} exception.
     *
     * @return The strategy where was added.
     * @see #add(Exception)
     */
    public StrategyHttpMock addSocketException() {
        responsesException.add(new SocketException(DETAIL_MESSAGE));
        return this;
    }

    /**
     * Adds a {@link MalformedURLException} exception.
     *
     * @return The strategy where was added.
     * @see #add(Exception)
     */
    public StrategyHttpMock addMalformedURLException() {
        responsesException.add(new MalformedURLException(DETAIL_MESSAGE));
        return this;
    }

    /**
     * Adds a {@link TimeoutException} exception.
     *
     * @return The strategy where was added.
     * @see #add(Exception)
     */
    public StrategyHttpMock addTimeoutException() {
        responsesException.add(new TimeoutException(DETAIL_MESSAGE));
        return this;
    }

    /**
     * Adds a {@link IOException} exception.
     *
     * @return The strategy where was added.
     * @see #add(Exception)
     */
    public StrategyHttpMock addIOException() {
        responsesException.add(new IOException(DETAIL_MESSAGE));
        return this;
    }

    /**
     * Adds a default {@link Exception}.
     *
     * @return The strategy where was added.
     * @see #add(Exception)
     */
    public StrategyHttpMock addException() {
        responsesException.add(new Exception(DETAIL_MESSAGE));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StrategyHttpMock)) return false;

        StrategyHttpMock that = (StrategyHttpMock) o;

        return responses.equals(that.responses);
    }

    @Override
    public int hashCode() {
        return responses.hashCode();
    }

}