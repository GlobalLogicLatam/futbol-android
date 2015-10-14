package com.globallogic.futbol.core.operation.strategies;

import java.io.File;

public class StrategyFileMockResponse extends StrategyMockResponse<File> {

    public StrategyFileMockResponse(int httpCode, String assetFilePath) {
        setHttpCode(httpCode);
        setFilePath(assetFilePath);
    }

    public int getHttpCode() {
        return getIntCode();
    }

    public void setHttpCode(int httpCode) {
        setIntCode(httpCode);
    }

    public String getFilePath() {
        return getStatus();
    }

    public void setFilePath(String filePath) {
        setStatus(filePath);
    }

}