package com.globallogic.futbol.example.presenters;

import android.content.Context;
import android.view.View;

import com.globallogic.futbol.example.domain.models.Device;

/**
 * @author facundo.mengoni
 * @since 0.3
 */
public interface IGetDevicesView {
    Context getActivity();

    void goToDeviceDetail(Device device);

    void showSnackBar(String message, int duration, String actionTitle, View.OnClickListener onClickListener);

    void changeWaitingView(Boolean isWaiting);

    void changeNormalView(Boolean hasError);

    void setErrorMessage(String error);
}
