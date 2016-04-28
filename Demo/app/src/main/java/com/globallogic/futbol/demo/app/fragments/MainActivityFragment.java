package com.globallogic.futbol.demo.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewSwitcher;

import com.globallogic.futbol.demo.R;
import com.globallogic.futbol.demo.app.adapters.MainAdapter;
import com.globallogic.futbol.demo.app.operations.CreateDeviceOperation;
import com.globallogic.futbol.demo.app.operations.GetDevicesOperation;
import com.globallogic.futbol.demo.domain.Device;

import java.util.ArrayList;

public class MainActivityFragment extends Fragment implements GetDevicesOperation.IGetDevicesOperation {
    public static final String TAG = MainActivityFragment.class.getSimpleName();

    public static MainActivityFragment newInstance() {
        return new MainActivityFragment();
    }

    private final GetDevicesOperation mGetDevicesOperation;
    private final GetDevicesOperation.GetDevicesReceiver mGetDevicesReceiver;
    private final CreateDeviceOperation.CreateDeviceReceiver mCreateDeviceReceiver;
    private RecyclerView vRecyclerView;
    private MainAdapter mAdapter;
    private ViewSwitcher vWaitingSwitcher;

    public MainActivityFragment() {
        mGetDevicesOperation = new GetDevicesOperation();
        mGetDevicesReceiver = new GetDevicesOperation.GetDevicesReceiver(this);
        mCreateDeviceReceiver = new CreateDeviceOperation.CreateDeviceReceiver(new CreateDeviceOperation.ICreateDeviceOperation() {
            @Override
            public void onNoInternet() {

            }

            @Override
            public void onError(@StringRes int resError) {

            }

            @Override
            public void onSuccess(Device device) {
                mAdapter.addItem(device);
            }


            @Override
            public void onStartOperation() {

            }

            @Override
            public void onFinishOperation() {

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGetDevicesOperation.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mGetDevicesReceiver.startListening(mGetDevicesOperation); // <-- Don't forgot register the receiver
        mCreateDeviceReceiver.startListening(CreateDeviceOperation.class);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        vWaitingSwitcher = (ViewSwitcher) view.findViewById(R.id.device_list_waiting);

        vRecyclerView = (RecyclerView) view.findViewById(R.id.device_list);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        vRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MainAdapter();
        if (savedInstanceState == null)
            doOperation();
        else
            mAdapter.onRestoreInstance(savedInstanceState);
        vRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void doOperation() {
        mGetDevicesOperation.performOperation();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mGetDevicesOperation.onSaveInstanceState(outState);
        mAdapter.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mGetDevicesReceiver.stopListening(); // <-- Don't forgot unregister the receiver
    }

    public void showWaiting(boolean show) {
        if (show) {
            if (vWaitingSwitcher.getDisplayedChild() == 0)
                vWaitingSwitcher.showNext();
        } else {
            if (vWaitingSwitcher.getDisplayedChild() == 1)
                vWaitingSwitcher.showPrevious();
        }
    }

    @Override
    public void onStartOperation() {
        if (mAdapter.getItemCount() == 0)
            showWaiting(true);
    }

    @Override
    public void onFinishOperation() {

    }

    @Override
    public void onSuccess(ArrayList<Device> devices) {
        showWaiting(false);
        mAdapter.addItems(devices);
    }

    @Override
    public void onError(int resError) {
        showWaiting(false);
    }

    @Override
    public void onNoInternet() {

    }
}