package com.globallogic.futbol.demo;

import android.app.Application;

import com.globallogic.futbol.core.OperationApp;

/**
 * Created by Facundo Mengoni on 6/12/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OperationApp.setInstance(this);
    }
}