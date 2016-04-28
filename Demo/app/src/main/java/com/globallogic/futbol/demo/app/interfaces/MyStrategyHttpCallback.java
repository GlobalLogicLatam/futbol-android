package com.globallogic.futbol.demo.app.interfaces;

import android.support.annotation.StringRes;

import com.globallogic.futbol.core.interfaces.callbacks.IStrategyHttpCallback;

/**
 * Default http callback for the demo
 * @author facundo.mengoni
 */
public interface MyStrategyHttpCallback extends IStrategyHttpCallback {
    void onError(@StringRes int resError);
}
