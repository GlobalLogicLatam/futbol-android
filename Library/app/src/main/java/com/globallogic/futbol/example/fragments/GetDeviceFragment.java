package com.globallogic.futbol.example.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.globallogic.futbol.example.R;
import com.globallogic.futbol.example.entities.Device;
import com.globallogic.futbol.example.operations.GetDeviceOperation;

/**
 * Created by Ezequiel Sanz on 11/05/15.
 * GlobalLogic | ezequiel.sanz@globallogic.com
 */
public class GetDeviceFragment extends Fragment implements GetDeviceOperation.IGetDeviceReceiver {
    public static final String TAG = "GetDeviceFragment";
    // Defino mi operacion y mi receiver
    private final GetDeviceOperation mGetDeviceOperation;
    private final GetDeviceOperation.GetDeviceReceiver mGetDeviceReceiver;
    private TextView vOperationStatus;
    private TextView vOperationResult;

    public GetDeviceFragment() {
        String id = "1";
        mGetDeviceOperation = new GetDeviceOperation(id);
        mGetDeviceReceiver = new GetDeviceOperation.GetDeviceReceiver(this);
    }

    public static GetDeviceFragment newInstance() {
        return new GetDeviceFragment();
    }

    @Override
    public void onNoInternet() {
        // ToDo
    }

    @Override
    public void onStartOperation() {
        updateOperationStatus();
    }

    @Override
    public void onSuccess(Device aDevice) {
        updateOperationStatus();
        vOperationResult.setText(aDevice.toString());
    }

    @Override
    public void onError() {
        updateOperationStatus();
        vOperationResult.setText(mGetDeviceOperation.getError(getActivity()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGetDeviceOperation.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_get_example_get_device, container, false);
        vOperationStatus = (TextView) rootView.findViewById(R.id.operation_status);
        vOperationResult = (TextView) rootView.findViewById(R.id.operation_result);

        mGetDeviceReceiver.register(mGetDeviceOperation);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        submit();
    }

    private void submit() {
        mGetDeviceOperation.performOperation();
    }

    private void updateOperationStatus() {
        switch (mGetDeviceOperation.getStatus()) {
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
        mGetDeviceOperation.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Desregistro el receiver
        mGetDeviceReceiver.unRegister();
    }
}