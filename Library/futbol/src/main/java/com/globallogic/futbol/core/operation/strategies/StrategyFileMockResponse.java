package com.globallogic.futbol.core.operation.strategies;

import java.io.File;

public class StrategyFileMockResponse extends StrategyMockResponse<File> {

    public enum FileLocation {
        ASSETS,
        SD_CARD
    }

    private FileLocation mLocation;

    public StrategyFileMockResponse(int httpCode, String assetFilePath, FileLocation location) {
        setHttpCode(httpCode);
        setFilePath(assetFilePath);
        setLocation(location);
    }

    public FileLocation getLocation() {
        return mLocation;
    }

    public void setLocation(FileLocation mLocation) {
        this.mLocation = mLocation;
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