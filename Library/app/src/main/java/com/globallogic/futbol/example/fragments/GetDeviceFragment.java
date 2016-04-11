package com.globallogic.futbol.example.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.globallogic.futbol.example.R;
import com.globallogic.futbol.example.presenters.GetDevicePresenter;
import com.globallogic.futbol.example.presenters.IGetDeviceView;

/**
 * Created by Ezequiel Sanz on 11/05/15.
 * GlobalLogic | ezequiel.sanz@globallogic.com
 */
public class GetDeviceFragment extends Fragment implements IGetDeviceView {
    private static final String ARGUMENT_ID = "ARGUMENT_ID";
    public static final String TAG = "GetDeviceFragment";

    //region View vars
    private TextView vOperationResult;
    private ViewSwitcher vOperationWaitingContainer;
    //endregion

    private GetDevicePresenter mGetDevicesPresenter = new GetDevicePresenter(this);

    public static GetDeviceFragment newInstance(Integer anId) {
        GetDeviceFragment getDeviceFragment = new GetDeviceFragment();
        Bundle arg = new Bundle();
        arg.putInt(ARGUMENT_ID, anId);
        getDeviceFragment.setArguments(arg);
        return getDeviceFragment;
    }

    //region Lifecycle implementation
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGetDevicesPresenter.onCreate(getArguments().getInt(ARGUMENT_ID));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_get_example_get_device, container, false);
        vOperationResult = (TextView) rootView.findViewById(R.id.operation_result);
        vOperationWaitingContainer = (ViewSwitcher) rootView.findViewById(R.id.operation_waiting_container);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mGetDevicesPresenter.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mGetDevicesPresenter.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGetDevicesPresenter.onDestroy();
    }
    //endregion

    //region IGetDeviceView implementation
    @Override
    public void changeWaitingView(Boolean isWaiting) {
        if (isWaiting) {
            if (vOperationWaitingContainer.getDisplayedChild() == 0)
                vOperationWaitingContainer.showNext();
        } else {
            if (vOperationWaitingContainer.getDisplayedChild() == 1)
                vOperationWaitingContainer.showPrevious();
        }
    }

    @Override
    public void setErrorMessage(String error) {
        vOperationResult.setText(error);
    }

    @Override
    public void showDevice(String data) {
        vOperationResult.setText(data);
    }
    //endregion
}