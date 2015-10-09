package com.globallogic.futbol.core.operation.strategies;

import android.util.Log;

import com.globallogic.futbol.core.OperationApp;
import com.globallogic.futbol.core.interfaces.IOperationStrategy;
import com.globallogic.futbol.core.interfaces.IStrategyCallback;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * Created by Agustin Larghi on 08/10/2015.
 * Globallogic
 * agustin.larghi@globallogic.com
 */
public class StrategyFileMock implements IOperationStrategy {
    public static final String DETAIL_MESSAGE = "Hardcode dummy exception";
    private static final String TAG = StrategyFileMock.class.getSimpleName();
    protected final ArrayList<StrategyFileMockResponse> responses = new ArrayList<StrategyFileMockResponse>();
    protected final ArrayList<Exception> responsesException = new ArrayList<Exception>();
    private final Float mErrorProbability;
    private Random random = new Random();
    private IStrategyCallback mCallback;

    /**
     * A StrategyHttpMock allows to simulate any response that the server can return.
     *
     * @param errorProbability Determines the probability that the error occurs. If it is less than or equal to 0 returns a {@link StrategyHttpMockResponse} (or an exception if there is no added replies). If it is greater or equal to 1 always returns an exception
     */
    public StrategyFileMock(float errorProbability) {
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
            Exception mockException = getMockException();
            if (mockException != null)
                mCallback.parseResponse(mockException, new FileOperationResponse(0, null));
            else
                onNotResponseAdded();
        } else {
            // Retorno alguna respuesta dummy
            StrategyFileMockResponse mockResponse = getMockResponse();
            if (mockResponse != null) {
                File fileInCache = copyFileFromAssetsToCache(mockResponse.getFilePath());
                mCallback.parseResponse(null, new FileOperationResponse(mockResponse.getHttpCode(), fileInCache));
            } else {
                onNotResponseAdded();
            }
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

    private void onNotResponseAdded() {
        mCallback.parseResponse(new Exception(DETAIL_MESSAGE), new FileOperationResponse(0, null));
    }

    private StrategyFileMockResponse getMockResponse() {
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
     * @see StrategyHttpMockResponse
     */
    public StrategyFileMock add(StrategyFileMockResponse mockResponse) {
        responses.add(mockResponse);
        return this;
    }

    /**
     * Adds an expected exception
     */
    public StrategyFileMock add(Exception exception) {
        responsesException.add(exception);
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

    public StrategyFileMock addException() {
        responsesException.add(new Exception(DETAIL_MESSAGE));
        return this;
    }

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
}
