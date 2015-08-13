package com.globallogic.futbol.demo.app.operations;

import android.content.Context;
import android.content.Intent;

import com.globallogic.futbol.core.operation.Operation;
import com.globallogic.futbol.core.operation.OperationHelper;
import com.globallogic.futbol.demo.R;

/**
 * Created by Facundo Mengoni on 2015-08-12.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public abstract class MyOperation extends Operation {
    protected int mError = R.string.empty_error;

    public MyOperation() {
        setConnectionDelay(3 * 1000);
    }

    @Override
    public void reset() {
        super.reset();
        mError = R.string.empty_error;
    }

    @Override
    public void analyzeException(Exception e) {
        OperationHelper.analyzeException(e, new OperationHelper.ExceptionCallback() {
            @Override
            public void jsonSyntaxException() {
                mError = R.string.json_syntax_exception;
            }

            @Override
            public void timeOutException() {
                mError = R.string.time_out_exception;
            }

            @Override
            public void socketException() {
                mError = R.string.socket_exception;
            }

            @Override
            public void malformedURLException() {
                mError = R.string.malformed_url_exception;
            }

            @Override
            public void ioException() {
                mError = R.string.io_exception;
            }

            @Override
            public void otherException() {
                mError = R.string.other_exception;
            }
        });
    }

    @Override
    protected void addExtrasForResultError(Intent intent) {
        // Nothing in this case
    }

    @Override
    public String getError(Context context) {
        return context.getString(mError);
    }
}