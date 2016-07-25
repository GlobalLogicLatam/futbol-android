package com.globallogic.futbol.core.strategies.mock;

import android.os.Handler;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategyDbAnalyzer;
import com.globallogic.futbol.core.operations.Operation;
import com.globallogic.futbol.core.responses.StrategyDbResponse;
import com.globallogic.futbol.core.responses.StrategyHttpResponse;
import com.globallogic.futbol.core.strategies.DbOperationStrategy;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * It allows mock responses from the DB.
 *
 * @param <T> The class of the expected response.
 */
public class StrategyDbMock<T> extends DbOperationStrategy<T> {
    public static final String DETAIL_MESSAGE = "Hardcode dummy exception";
    protected final ArrayList<StrategyDbResponse<T>> responses = new ArrayList<>();
    protected final ArrayList<Exception> responsesException = new ArrayList<>();
    private final Float mErrorProbability;
    private Random random = new Random();
    private boolean wasCanceled;

    /**
     * A StrategyDbMock allows to simulate any response that the database can return.
     *
     * @param analyzer         An analyzer for the responses mocked.
     * @param errorProbability Determines the probability that the error occurs. If it is less than or equal to 0 returns a {@link StrategyHttpResponse} (or an exception if there is no added replies). If it is greater or equal to 1 always returns an exception
     */
    public StrategyDbMock(Operation anOperation, IStrategyDbAnalyzer<T> analyzer, float errorProbability) {
        super(anOperation, analyzer);
        this.mErrorProbability = errorProbability;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doRequestImpl() {
        if (!wasCanceled) {
            if (random.nextFloat() < mErrorProbability) {
                // Ejecuto un error
                Exception mockException = getMockException();
                if (mockException != null)
                    parseResponse(mockException, null);
                else
                    onNotResponseAdded();
            } else {
                // Retorno alguna respuesta dummy
                StrategyDbResponse<T> mockResponse = getMockResponse();
                if (mockResponse != null)
                    parseResponse(null, mockResponse);
                else
                    onNotResponseAdded();
            }
        }
    }

    @Override
    public void cancel() {
        wasCanceled = true;
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
    private StrategyDbResponse<T> getMockResponse() {
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
     * @see StrategyDbResponse
     */
    public StrategyDbMock add(StrategyDbResponse<T> mockResponse) {
        responses.add(mockResponse);
        return this;
    }

    /**
     * Adds an expected exception.
     *
     * @param exception The exception to add.
     * @return The strategy where was added.
     */
    public StrategyDbMock add(Exception exception) {
        responsesException.add(exception);
        return this;
    }

    /**
     * Adds a {@link JsonSyntaxException} exception.
     *
     * @return The strategy where was added.
     * @see #add(Exception)
     */
    public StrategyDbMock addJsonSyntaxException() {
        responsesException.add(new JsonSyntaxException(DETAIL_MESSAGE));
        return this;
    }

    /**
     * Adds a {@link SocketException} exception.
     *
     * @return The strategy where was added.
     * @see #add(Exception)
     */
    public StrategyDbMock addSocketException() {
        responsesException.add(new SocketException(DETAIL_MESSAGE));
        return this;
    }

    /**
     * Adds a {@link MalformedURLException} exception.
     *
     * @return The strategy where was added.
     * @see #add(Exception)
     */
    public StrategyDbMock addMalformedURLException() {
        responsesException.add(new MalformedURLException(DETAIL_MESSAGE));
        return this;
    }

    /**
     * Adds a {@link TimeoutException} exception.
     *
     * @return The strategy where was added.
     * @see #add(Exception)
     */
    public StrategyDbMock addTimeoutException() {
        responsesException.add(new TimeoutException(DETAIL_MESSAGE));
        return this;
    }

    /**
     * Adds a {@link IOException} exception.
     *
     * @return The strategy where was added.
     * @see #add(Exception)
     */
    public StrategyDbMock addIOException() {
        responsesException.add(new IOException(DETAIL_MESSAGE));
        return this;
    }

    /**
     * Adds a default {@link Exception}.
     *
     * @return The strategy where was added.
     * @see #add(Exception)
     */
    public StrategyDbMock addException() {
        responsesException.add(new Exception(DETAIL_MESSAGE));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StrategyDbMock)) return false;

        StrategyDbMock that = (StrategyDbMock) o;

        return responses.equals(that.responses);
    }

    @Override
    public int hashCode() {
        return responses.hashCode();
    }

}