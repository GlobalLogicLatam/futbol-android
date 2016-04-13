package com.globallogic.futbol.example.presenters;

import android.content.Context;
import android.view.View;

import com.globallogic.futbol.example.domain.models.Device;

/**
 * @author facundo.mengoni
 * @since 0.3
 */
public interface IGetDeviceView {
    Context getActivity();

    void changeWaitingView(Boolean isWaiting);

    void setErrorMessage(String error);

    void showDevice(String data);
}
