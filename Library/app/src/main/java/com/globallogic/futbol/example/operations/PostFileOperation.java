package com.globallogic.futbol.example.operations;

import android.content.Intent;
import android.os.Bundle;

import com.globallogic.futbol.core.OperationResponse;
import com.globallogic.futbol.core.interfaces.IOperationStrategy;
import com.globallogic.futbol.core.operation.OperationBroadcastReceiver;
import com.globallogic.futbol.core.operation.strategies.StrategyFileMock;
import com.globallogic.futbol.core.operation.strategies.StrategyFileMockResponse;
import com.globallogic.futbol.example.operations.helper.ExampleOperationFile;

import java.io.File;
import java.net.HttpURLConnection;

/**
 * Created by Agustin Larghi on 08/10/2015.
 * Globallogic
 * agustin.larghi@globallogic.com
 */
public class PostFileOperation extends ExampleOperationFile {

    private static final String TAG = PostFileOperation.class.getSimpleName();
    private static final String SAVE_INSTANCE_FILE = "SAVE_INSTANCE_FILE";

    private File mFile;

    public PostFileOperation() {
        super("");
    }

    @Override
    public void reset() {
        super.reset();
        mFile = null;
    }

    public void execute(File fileToPost) {
        reset();
        performOperation(fileToPost);
    }

    @Override
    protected IOperationStrategy getStrategy(Object... arg) {
        File fileToPost = (File) arg[0];
        StrategyFileMock strategyFileMock = new StrategyFileMock(0f);
        strategyFileMock.add(new StrategyFileMockResponse(HttpURLConnection.HTTP_OK, fileToPost.getAbsolutePath()));
        return strategyFileMock;
    }

    @Override
    public Boolean analyzeResult(OperationResponse<Integer, File> response) {
        switch (response.getResultCode()) {
            case HttpURLConnection.HTTP_OK:
                this.mFile = response.getResult();
                return true;
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mFile != null)
            outState.putSerializable(SAVE_INSTANCE_FILE, mFile);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(SAVE_INSTANCE_FILE))
            mFile = (File) savedInstanceState.getSerializable(SAVE_INSTANCE_FILE);
    }

    @Override
    protected void addExtrasForResultOk(Intent intent) {
        intent.putExtra(Receiver.EXTRA_DEVICE, mFile);
    }

    public interface Callback {
        void onNoInternet();

        void onStartOperation();

        void onSuccess(File aFile);

        void onError();

        void onFinishOperation();
    }

    public static class Receiver extends OperationBroadcastReceiver {
        static final String EXTRA_DEVICE = "EXTRA_DEVICE";
        private final Callback mCallback;

        public Receiver(Callback callback) {
            super();
            mCallback = callback;
        }

        @Override
        protected void onNoInternet() {
            mCallback.onNoInternet();
        }

        @Override
        protected void onStartOperation() {
            mCallback.onStartOperation();
        }

        protected void onResultOK(Intent anIntent) {
            File file = (File) anIntent.getSerializableExtra(EXTRA_DEVICE);
            if (file != null)
                mCallback.onSuccess(file);
            else
                mCallback.onError();
        }

        protected void onResultError(Intent anIntent) {
            mCallback.onError();
        }

        @Override
        protected void onFinishOperation() {
            mCallback.onFinishOperation();
        }
    }

}
