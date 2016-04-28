package com.globallogic.futbol.demo.app.analyzers;

import android.content.Intent;

import com.globallogic.futbol.core.interfaces.analyzers.IStrategyHttpAnalyzer;
import com.globallogic.futbol.core.operations.OperationHelper;
import com.globallogic.futbol.demo.R;
import com.globallogic.futbol.demo.app.receivers.MyReceiver;
import com.globallogic.futbol.demo.domain.Device;

import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Default http analyzer for the demo
 * @author facundo.mengoni
 */
public abstract class MyDefaultHttpAnalyzer implements IStrategyHttpAnalyzer {
    protected int mError = R.string.empty_error;

    @Override
    public void addExtrasForResultError(Intent intent) {
        intent.putExtra(MyReceiver.ERROR, mError);
    }

    @Override
    public void analyzeException(Exception anException) {
        OperationHelper.analyzeException(anException, new OperationHelper.ExceptionCallback() {
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

            @Override
            public void unexpectedResponseException() {
                mError = R.string.unexpected_response_exception;
            }
        });
    }
}