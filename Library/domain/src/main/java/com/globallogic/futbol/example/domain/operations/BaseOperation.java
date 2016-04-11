package com.globallogic.futbol.example.domain.operations;

import android.content.Context;
import android.content.Intent;

import com.globallogic.futbol.core.operations.Operation;
import com.globallogic.futbol.core.operations.OperationHelper;
import com.globallogic.futbol.example.domain.R;

import java.util.concurrent.TimeUnit;

/**
 * Created by Facundo Mengoni on 6/3/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public abstract class BaseOperation extends Operation {
    public static final int SECONDS = 3;
    public int errorResource = R.string.no_error;

    public BaseOperation() {
        init();
    }

    public BaseOperation(String id) {
        super(id);
        init();
    }

    private void init() {
        setConnectionDelay(TimeUnit.SECONDS.toMillis(SECONDS));
    }

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

    protected void addExtrasForResultError(Intent intent) {
    }

    public String getError(Context aContext) {
        return aContext.getString(errorResource);
    }
}