package com.globallogic.futbol.example.operations.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.globallogic.futbol.core.operation.Operation;
import com.globallogic.futbol.core.operation.OperationHelper;
import com.globallogic.futbol.example.R;

import java.util.concurrent.TimeUnit;

/**
 * Created by Facundo Mengoni on 6/3/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public abstract class ExampleOperation extends Operation {
    public static final String ERROR_RESOURCE = "ERROR_RESOURCE";
    public static final int SECONDS = 3;
    public int errorResource = R.string.no_error;

    public ExampleOperation() {
        init();
    }

    public ExampleOperation(String id) {
        super(id);
        init();
    }

    private void init() {
        setConnectionDelay(TimeUnit.SECONDS.toMillis(SECONDS));
    }

    @Override
    public void reset() {
        super.reset();
        errorResource = R.string.no_error;
    }

    @Override
    public void analyzeException(Exception e) {
        OperationHelper.analyzeException(e, new OperationHelper.ExceptionCallback() {
            @Override
            public void jsonSyntaxException() {
                errorResource = R.string.error_json_syntax_exception;
            }

            @Override
            public void timeOutException() {
                errorResource = R.string.error_time_out_exception;
            }

            @Override
            public void socketException() {
                errorResource = R.string.error_socket_exception;
            }

            @Override
            public void malformedURLException() {
                errorResource = R.string.error_malformed_url_exception;
            }

            @Override
            public void ioException() {
                errorResource = R.string.error_io_exception;
            }

            @Override
            public void otherException() {
                errorResource = R.string.error_other_exception;
            }

            @Override
            public void unexpectedResponseException() {
                errorResource = R.string.error_unexpected_response_exception;
            }
        });
    }

    @Override
    protected void addExtrasForResultError(Intent intent) {
    }

    @Override
    public String getError(Context aContext) {
        return aContext.getString(errorResource);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ERROR_RESOURCE, errorResource);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        errorResource = savedInstanceState.getInt(ERROR_RESOURCE);
    }
}