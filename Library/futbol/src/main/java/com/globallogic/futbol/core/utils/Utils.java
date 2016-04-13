package com.globallogic.futbol.core.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;

import com.globallogic.futbol.BuildConfig;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author facundo.mengoni
 * @since 0.3
 */
public class Utils {
    private final static String TAG = Utils.class.toString();
    public static boolean isDebug = BuildConfig.DEBUG;

    //region Logger
    public static Logger sLogger;

    static {
        sLogger = Logger.getLogger(TAG);
        if (BuildConfig.DEBUG)
            sLogger.setLevel(Level.ALL);
        else
            sLogger.setLevel(Level.OFF);
    }
    //endregion

    public static Boolean isThreadUI() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public static void thisMethodShouldNotExecuteInTheThreadUI() {
        if (isDebug)
            if (isThreadUI()) {
                throw new RuntimeException("This method should not execute in the thread UI");
            }
    }

    public static boolean hasInternet(Context aContext) {
        ConnectivityManager cm = (ConnectivityManager) aContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            sLogger.info("No internet");
            return Boolean.FALSE;
        }
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) {
            sLogger.info("No internet");
            return Boolean.FALSE;
        }
        boolean connected = networkInfo.isConnected();
        if (connected)
            sLogger.info("No internet");
        else
            sLogger.info("Has internet");
        return connected;
    }
}