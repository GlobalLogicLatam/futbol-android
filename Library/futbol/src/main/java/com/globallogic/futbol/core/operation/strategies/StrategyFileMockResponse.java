package com.globallogic.futbol.core.operation.strategies;

import java.io.Serializable;

public class StrategyFileMockResponse implements Serializable {
    private Integer httpCode;
    private String filePath;

    public StrategyFileMockResponse() {
    }

    public StrategyFileMockResponse(int httpCode, String assetFilePath) {
        this.httpCode = httpCode;
        this.filePath = assetFilePath;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "StrategyHttpMockResponse{" +
                "httpCode=" + httpCode +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}