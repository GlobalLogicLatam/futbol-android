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
import com.globallogic.futbol.example.activities.GenericExampleActivity;
import com.globallogic.futbol.example.domain.models.Device;
import com.globallogic.futbol.example.domain.operations.GetDeviceOperation;
import com.globallogic.futbol.example.presenters.GetDevicesPresenter;
import com.globallogic.futbol.example.presenters.IGetDevicesView;

/**
 * Created by Ezequiel Sanz on 11/05/15.
 * GlobalLogic | ezequiel.sanz@globallogic.com
 */
public class GetDevicesFragment extends Fragment implements IGetDevicesView {
    public static final String TAG = "GetDevicesFragment";

    //region View vars
    private TextView vOperationResult;
    private ViewSwitcher vOperationResultContainer;
    private ViewSwitcher vOperationWaitingContainer;
    private RecyclerView vRecyclerView;
    //endregion

    private GetDevicesPresenter mGetDevicesPresenter = new GetDevicesPresenter(this);

    public static GetDevicesFragment newInstance() {
        return new GetDevicesFragment();
    }

    //region Lifecycle implementation
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGetDevicesPresenter.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_get_example_get_list, container, false);
        vOperationResult = (TextView) rootView.findViewById(R.id.operation_result);
        vOperationResultContainer = (ViewSwitcher) rootView.findViewById(R.id.operation_result_container);
        vOperationWaitingContainer = (ViewSwitcher) rootView.findViewById(R.id.operation_waiting_container);
        vRecyclerView = (RecyclerView) rootView.findViewById(R.id.operation_list);
        vRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        vRecyclerView.setAdapter(mGetDevicesPresenter.getMediasAdapter());

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

    //region IGetDevicesView implementation
    @Override
    public void goToDeviceDetail(Device device) {
        startActivity(GenericExampleActivity.generateIntent(getActivity(), GetDeviceFragment.TAG, device.getId()));
    }

    @Override
    public void showSnackBar(String message, int duration, String actionTitle, View.OnClickListener onClickListener) {
        Snackbar.make(vOperationResultContainer, message, duration).setAction(actionTitle, onClickListener).show();
    }

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
    public void changeNormalView(Boolean hasError) {
        if (hasError) {
            if (vOperationResultContainer.getDisplayedChild() == 1)
                vOperationResultContainer.showPrevious();
        } else {
            if (vOperationResultContainer.getDisplayedChild() == 0)
                vOperationResultContainer.showNext();
        }
    }

    @Override
    public void setErrorMessage(String error) {
        vOperationResult.setText(error);
    }
    //endregion
}