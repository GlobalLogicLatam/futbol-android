package com.globallogic.futbol.core.operation.strategies;

import java.io.Serializable;

/**
 * Created by Agustin Larghi on 14/10/2015.
 * Globallogic
 * agustin.larghi@globallogic.com
 * <p/>
 * A StrategyMockResponse is a wrapper of a T stream. This T stream can be a File if the
 * StrategyMockResponse is going to be used to wrap a File response, can be a String if the
 * StrategyMockResponse is going to be used to wrap a Json response, or can wrap a Cursor if is
 * going to wrap a database response.
 * <p/>
 * <p>Warning: There's no check of T, T can be of any type that inherits from the Object class</p>
 * <p/>
 * {@see StrategyHttpMockResponse}
 * {@see StrategyFileMockResponse}
 * {@see StrategySqliteMockResponse}
 */
public class StrategyMockResponse<T> implements Serializable {
    //region FIXME
    //FIXME Change this for a Parceable and leave the Parceable deserialization/serialization implementation to the subclasses
    private Integer intCode; //For example Http status codes
    private String status; //Status description this can be the path where a downloaded file was stored, the query that an operation made against the db
    //endregion
    private T stream; //Stream can be String (JSON), File or Cursor so far

    public Integer getIntCode() {
        return intCode;
    }

    public void setIntCode(Integer intCode) {
        this.intCode = intCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getStream() {
        return stream;
    }

    public void setStream(T stream) {
        this.stream = stream;
    }

    @Override
    public String toString() {
        return "StrategyMockResponse{" +
                "intCode=" + intCode +
                ", status='" + status + '\'' +
                ", stream=" + stream.toString() +
                '}';
    }
}
