package com.globallogic.futbol.core.operation;

import android.util.Log;

import com.globallogic.futbol.core.OperationApp;
import com.globallogic.futbol.core.exceptions.UnexpectedResponseException;
import com.globallogic.futbol.core.operation.strategies.StrategyFileMockResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

/**
 * Created by Agustin Larghi on 07/10/2015.
 * Globallogic
 * agustin.larghi@globallogic.com
 */
public abstract class OperationFile extends Operation<Integer, File> {

    private String targetPath;

    //region Constructors

    /**
     * Create a new instance with an id empty.
     * <p/>
     * The id is used to register the receiver for a specific operation.
     * If you register two operation with different ids then the receiver
     * of one operation never listen the other operation.
     */
    protected OperationFile() {
        this("");
    }

    protected OperationFile(String id, String filePath) {
        mLogger.info(String.format("Constructor with id: %s", id));
        this.id = id;
        this.targetPath = filePath;
        reset();
    }

    /**
     * Create a new instance with the specified id.
     * <p/>
     * The id is used to register the receiver for a specific operation.
     * If you register two operation with different ids then the receiver
     * of one operation never listen the other operation.
     */
    protected OperationFile(String id) {
        mLogger.info(String.format("Constructor with id: %s", id));
        this.id = id;
        reset();
    }
    //endregion

    //region Methods for test

    /**
     * Run the operation synchronously and return the response expected in the callback of the broadcast
     *
     * @see OperationHttp#beforeWorkInBackground()
     * @see OperationHttp#workInBackground(Exception, Integer, String)
     * @see OperationHttp#afterWorkInBackground(Boolean)
     */
    protected Boolean testResponse(StrategyFileMockResponse aMockResponse) {
        mLogger.info(String.format("Test response: %s", aMockResponse.toString()));
        switch (mOperationStatus) {
            default:
            case UNKNOWN:
            case READY_TO_EXECUTE:
            case WAITING_EXECUTION:
                if (!hasInternet()) {
                    sendBroadcastForNoInternet();
                    return false;
                }
                beforeWorkInBackground();
                Boolean result = workInBackground(null, aMockResponse.getHttpCode(), copyFileFromAssetsToCache(aMockResponse.getFilePath()));
                afterWorkInBackground(result);
                return true;
            case FINISHED_EXECUTION:
                afterWorkInBackgroundBroadcasts(mResult);
                return false;
            case DOING_EXECUTION:
                beforeWorkInBackgroundBroadcasts();
                return false;
        }
    }

    private File copyFileFromAssetsToCache(String assetPath) {
        InputStream in = null;
        OutputStream out = null;
        File outFile = null;
        try {
            in = OperationApp.getInstance().getAssets().open(assetPath);
            String parentPath = ((targetPath == null) ? (OperationApp.getInstance().getExternalFilesDir(null).getAbsolutePath()) : (targetPath));
            mLogger.info(String.format("Copying mocked file from assets %s into %s", assetPath, parentPath));
            outFile = new File(parentPath, assetPath);
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
    //endregion

    //region IOperation
    /**
     * Analyze the parameters to determine what would do
     *
     * @param anException The exception occurred
     * @param aHttpCode   The httpCode obtained
     * @see OperationHttp#analyzeException(Exception)
     */
    protected Boolean workInBackground(Exception anException, Integer aHttpCode, File aFile) {
        mLogger.info("Work in background");
        if (anException != null) {
            analyzeException(anException);
            return false;
        } else {
            try {
                if (!analyzeResult(new FileOperationResponse(aHttpCode, aFile)))
                    throw new UnexpectedResponseException();
            } catch (Exception e2) {
                mLogger.log(Level.INFO, "Error in analyzeResult: " + e2.getMessage(), e2);
                analyzeException(e2);
                return false;
            }
            return true;
        }
    }
    //endregion

}
