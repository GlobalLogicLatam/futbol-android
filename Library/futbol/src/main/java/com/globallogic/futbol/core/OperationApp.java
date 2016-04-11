package com.globallogic.futbol.core;


import android.app.Application;

/**
 * This is an application that it will use as context for the library.
 * If you have your own application you should set it in this class with the {@link #setInstance(Application)} method.
 *
 * @author facundo.mengoni
 * @since 0.1.0
 */
public class OperationApp extends Application {
    private static final String TAG = OperationApp.class.toString();

    private static Application singleton;

    public static Application getInstance() {
        return singleton;
    }

    public static void setInstance(Application application) {
        singleton = application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
    }
}