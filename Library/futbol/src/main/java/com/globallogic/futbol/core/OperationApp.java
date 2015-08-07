package com.globallogic.futbol.core;


import android.app.Application;

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