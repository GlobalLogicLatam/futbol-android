package com.globallogic.futbol.core.operation.strategies;

import com.globallogic.futbol.core.interfaces.IOperationStrategy;
import com.globallogic.futbol.core.interfaces.IStrategyCallback;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Agustin Larghi on 14/10/2015.
 * Globallogic
 * agustin.larghi@globallogic.com
 * <p/>
 * StrategyMock is a template method that you can use to implement your own mocked strategies.
 * You can hook of two methods: #executeFailedMockRequest Which performs a failed mocked request with a
 * fake fail exception and fake messages and #executeSuccessfulMockRequest which performs a success
 * mocked request.
 * <p/>
 * <p>Is important in order to fully comprehend this class checking the subclasses:</p>
 * <p/>
 * {@see StrategyFileMock}
 * {@see StrategySqliteMock}
 * {@see StrategyHttpMock}
 */
public abstract class StrategyMock<T extends StrategyMockResponse> implements IOperationStrategy {

    //region Constants
    public static final String DETAIL_MESSAGE = "Hardcode dummy exception";
    private static final String TAG = StrategyFileMock.class.getSimpleName();
    //endregion

    //region Variables
    protected final ArrayList<Exception> responsesException = new ArrayList<Exception>();
    protected final Float mErrorProbability;
    protected Random random = new Random();
    protected IStrategyCallback mCallback;
    protected final ArrayList<StrategyMockResponse> responses = new ArrayList<StrategyMockResponse>();
    //endregion

    //region Public API
    protected Exception getMockException() {
        if (responsesException.size() > 0) {
            return responsesException.get(random.nextInt(responsesException.size()));
        }
        return null;
    }

    public StrategyMockResponse getMockResponse() {
        if (responses.size() > 0) {
            return responses.get(random.nextInt(responses.size()));
        }
        return null;
    }

    /**
     * A StrategyHttpMock allows to simulate any response that the server can return.
     *
     * @param errorProbability Determines the probability that the error occurs. If it is less than or equal to 0 returns a {@link StrategyHttpMockResponse} (or an exception if there is no added replies). If it is greater or equal to 1 always returns an exception
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
        if (random.nextFloat() < mErrorProbability) {
            // Ejecuto un error
            executeFailedMockRequest();
        } else {
            // Retorno alguna respuesta dummy
            executeSuccessfulMockRequest();
        }
    }
    //endregion

    //region Hooks
    protected abstract void executeSuccessfulMockRequest();

    protected abstract void executeFailedMockRequest();
    //endregion

    //region Basic Mock Exceptions

    /**
     * Adds an expected exception
     */
    public StrategyMock add(Exception exception) {
        responsesException.add(exception);
        return this;
    }

    public StrategyMock addException() {
        responsesException.add(new Exception(DETAIL_MESSAGE));
        return this;
    }
    //endregion

    //region Equals and Hash methods
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

    public void add(StrategyMockResponse mockResponse) {
        responses.add(mockResponse);
    }
    //endregion
}
