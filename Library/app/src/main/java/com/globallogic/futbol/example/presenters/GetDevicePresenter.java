package com.globallogic.futbol.example.presenters;

import android.os.Bundle;
import android.widget.Toast;

import com.globallogic.futbol.example.R;
import com.globallogic.futbol.example.domain.models.Device;
import com.globallogic.futbol.example.domain.operations.GetDeviceOperation;

/**
 * @author facundo.mengoni
 * @since 0.3
 */
public class GetDevicePresenter {
    //region Constants
    private static final String SAVED_INSTANCE_DEVICE = "SAVED_INSTANCE_DEVICE";
    private static final String SAVED_INSTANCE_ID = "SAVED_INSTANCE_ID";
    //endregion

    private final IGetDeviceView view;

    //region Presenter vars
    private Device mDevice;

    //region Operation vars
    // Defino mi operacion y mi receiver
    private final GetDeviceOperation mGetDeviceOperation;
    private final GetDeviceOperation.GetDeviceReceiver mGetDeviceReceiver;
    private final GetDeviceOperation.IGetDeviceReceiver mGetDeviceCallback = new GetDeviceOperation.IGetDeviceReceiver() {
        @Override
        public void onNoInternet() {
            Toast.makeText(view.getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStartOperation() {
            view.changeWaitingView(true);
        }

        @Override
        public void onSuccess(Device aDevice) {
            mDevice = aDevice;
            view.changeWaitingView(false);
            view.showDevice(mDevice.toString());
        }

        @Override
        public void onError() {
            view.changeWaitingView(false);
            view.setErrorMessage(mGetDeviceOperation.getError(view.getActivity()));
        }

        @Override
        public void onFinishOperation() {
            //Nothing to do
        }
    };
    private Integer mId;
    //endregion
    //endregion

    public GetDevicePresenter(IGetDeviceView view) {
        this.view = view;
        mGetDeviceOperation = new GetDeviceOperation();
        mGetDeviceReceiver = new GetDeviceOperation.GetDeviceReceiver(mGetDeviceCallback);
    }

    //region Lifecycle implementation
    public void onCreate(Integer id) {
        mId = id;
        mGetDeviceOperation.setId(id.toString());
        mGetDeviceReceiver.startListening(mGetDeviceOperation);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SAVED_INSTANCE_DEVICE, mDevice);
        outState.putInt(SAVED_INSTANCE_ID, mId);
    }

    private void onRestoreInstance(Bundle savedInstanceState) {
        mDevice = (Device) savedInstanceState.getSerializable(SAVED_INSTANCE_DEVICE);
        mId = savedInstanceState.getInt(SAVED_INSTANCE_ID);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            onRestoreInstance(savedInstanceState);
            if (mDevice != null) {
                view.showDevice(mDevice.toString());
                view.changeWaitingView(false);
            }
        } else {
            mGetDeviceOperation.execute(mId);
        }
    }

    public void onDestroy() {
        mGetDeviceReceiver.stopListening();
    }
    //endregion

    //region Public methods implementation
    //endregion
}