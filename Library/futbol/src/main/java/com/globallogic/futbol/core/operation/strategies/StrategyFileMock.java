package com.globallogic.futbol.core.operation.strategies;

import android.util.Log;

import com.globallogic.futbol.core.OperationApp;
import com.globallogic.futbol.core.operation.FileOperationResponse;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Agustin Larghi on 08/10/2015.
 * Globallogic
 * agustin.larghi@globallogic.com
 */
public class StrategyFileMock extends StrategyMock<StrategyFileMockResponse> {

    public StrategyFileMock(float errorProbability) {
        super(errorProbability);
    }

    @Override
    protected void executeSuccessfulMockRequest() {
        StrategyFileMockResponse mockResponse = (StrategyFileMockResponse) getMockResponse();
        if (mockResponse != null) {
            File fileResult = null;
            switch (mockResponse.getLocation()) {
                case ASSETS:
                    fileResult = copyFileFromAssetsToCache(mockResponse.getFilePath());
                    break;
                case SD_CARD:
                    fileResult = copyFileFromSd(mockResponse.getFilePath());
                    break;
            }
            mCallback.parseResponse(null, new FileOperationResponse(mockResponse.getHttpCode(), fileResult));
        } else {
            onNotResponseAdded();
        }
    }

    protected void onNotResponseAdded() {
        mCallback.parseResponse(new Exception(DETAIL_MESSAGE), new FileOperationResponse(0, null));
    }

    @Override
    protected void executeFailedMockRequest() {
        Exception mockException = getMockException();
        if (mockException != null)
            mCallback.parseResponse(mockException, new FileOperationResponse(0, null));
        else
            onNotResponseAdded();
    }

    private File copyFileFromSd(String path) {
        File sdFile = new File(path);
        if (sdFile.exists()) {
            return sdFile;
        } else {
            return null;
        }
    }

    private File copyFileFromAssetsToCache(String assetPath) {
        InputStream in = null;
        OutputStream out = null;
        File outFile = null;
        try {
            in = OperationApp.getInstance().getAssets().open(assetPath);
            outFile = new File(OperationApp.getInstance().getExternalCacheDir(), assetPath);
            if (!outFile.exists()) {
                if (!outFile.getParentFile().exists()) {
                    outFile.getParentFile().mkdirs();
                }
                outFile.createNewFile();
            }
            out = new FileOutputStream(outFile);
            copyFile(in, out);
        } catch (IOException e) {
            Log.e("tag", "Failed to copy asset file: " + assetPath, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return outFile;
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    /**
     * Adds an expected response
     *
     * @see StrategyHttpMockResponse
     */
    public StrategyFileMock add(StrategyFileMockResponse mockResponse) {
        responses.add(mockResponse);
        return this;
    }

    public StrategyFileMock addJsonSyntaxException() {
        responsesException.add(new JsonSyntaxException(DETAIL_MESSAGE));
        return this;
    }

    public StrategyFileMock addSocketException() {
        responsesException.add(new SocketException(DETAIL_MESSAGE));
        return this;
    }

    public StrategyFileMock addMalformedURLException() {
        responsesException.add(new MalformedURLException(DETAIL_MESSAGE));
        return this;
    }

    public StrategyFileMock addTimeoutException() {
        responsesException.add(new TimeoutException(DETAIL_MESSAGE));
        return this;
    }

    public StrategyFileMock addIOException() {
        responsesException.add(new IOException(DETAIL_MESSAGE));
        return this;
    }

}
