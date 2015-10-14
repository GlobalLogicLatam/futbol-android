package com.globallogic.futbol.example.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.globallogic.futbol.example.R;
import com.globallogic.futbol.example.entities.Device;
import com.globallogic.futbol.example.operations.SelectDevicesOperation;

import java.util.ArrayList;

public class SelectDevicesFragment extends Fragment implements SelectDevicesOperation.ISelectDevicesCallback {
    public static final String TAG = "SelectDevicesFragment";

    private final SelectDevicesOperation mSelectDevicesOperation;
    private final SelectDevicesOperation.SelectDevicesReceiver mSelectDevicesReceiver;
    private TextView vOperationStatus;
    private TextView vOperationResult;

    public SelectDevicesFragment() {
        mSelectDevicesOperation = new SelectDevicesOperation();
        mSelectDevicesReceiver = new SelectDevicesOperation.SelectDevicesReceiver(this);
    }

    public static SelectDevicesFragment newInstance() {
        return new SelectDevicesFragment();
    }

    @Override
    public void onNoInternet() {
        Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartOperation() {
        updateOperationStatus();
    }

    @Override
    public void onSuccess(ArrayList<Device> dbDevices) {
        vOperationResult.setText(dbDevices.toString());
    }

    @Override
    public void onError() {
        vOperationResult.setText(mSelectDevicesOperation.getError(getActivity()));
    }

    @Override
    public void onFinishOperation() {
        updateOperationStatus();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSelectDevicesOperation.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_get_example_get_device, container, false);
        vOperationStatus = (TextView) rootView.findViewById(R.id.operation_status);
        vOperationResult = (TextView) rootView.findViewById(R.id.operation_result);

        mSelectDevicesReceiver.register(mSelectDevicesOperation);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSelectDevicesOperation.execute();
    }

    private void updateOperationStatus() {
        switch (mSelectDevicesOperation.getStatus()) {
            case READY_TO_EXECUTE:
                vOperationStatus.setText("Ready to execute");
                break;
            case WAITING_EXECUTION:
                vOperationStatus.setText("Waiting execution");
                break;
            case DOING_EXECUTION:
                vOperationStatus.setText("Doing execution");
                break;
            case FINISHED_EXECUTION:
                vOperationStatus.setText("Finished execution");
                break;
            case UNKNOWN:
            default:
                vOperationStatus.setText("Some error");
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Guardo los datos necesario de la operacion
        mSelectDevicesOperation.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Desregistro el receiver
        mSelectDevicesReceiver.unRegister();
    }
}