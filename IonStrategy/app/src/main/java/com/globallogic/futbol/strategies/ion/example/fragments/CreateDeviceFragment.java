package com.globallogic.futbol.strategies.ion.example.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.globallogic.futbol.strategies.ion.example.R;
import com.globallogic.futbol.strategies.ion.example.entities.Device;
import com.globallogic.futbol.strategies.ion.example.operations.CreateDeviceOperation;

/**
 * Created by Facundo Mengoni on 8/6/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class CreateDeviceFragment extends Fragment implements CreateDeviceOperation.ICreateDeviceReceiver, View.OnClickListener {
    public static final String TAG = "CreateDeviceFragment";
    private EditText vName;
    private EditText vResolution;
    private Button vSubmit;
    private TextView vOperationResult;

    // Defino mi operacion y mi receiver
    private final CreateDeviceOperation mCreateDeviceOperation;
    private final CreateDeviceOperation.CreateDeviceReceiver mCreateDeviceReceiver;


    public static CreateDeviceFragment newInstance() {
        return new CreateDeviceFragment();
    }

    public CreateDeviceFragment() {
        mCreateDeviceOperation = new CreateDeviceOperation();
        mCreateDeviceReceiver = new CreateDeviceOperation.CreateDeviceReceiver(this);
    }

    private void enableButtons(boolean value) {
        vName.setEnabled(value);
        vResolution.setEnabled(value);
        vSubmit.setEnabled(value);
    }

    @Override
    public void onNoInternet() {
        Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartOperation() {
        updateOperationStatus();
        enableButtons(false);
    }

    @Override
    public void onSuccess(Device aDevice) {
        vOperationResult.setText(aDevice.toString());
        enableButtons(true);
    }

    @Override
    public void onError() {
        vOperationResult.setText(mCreateDeviceOperation.getError(getActivity()));
        enableButtons(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCreateDeviceOperation.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_example_create_device, container, false);
        vName = (EditText) rootView.findViewById(R.id.fragment_post_example_submit_device_name);
        vResolution = (EditText) rootView.findViewById(R.id.fragment_post_example_submit_device_resolution);
        vSubmit = (Button) rootView.findViewById(R.id.fragment_post_example_submit_device_submit);
        vSubmit.setOnClickListener(this);
        vOperationResult = (TextView) rootView.findViewById(R.id.fragment_post_example_submit_device_result);

        mCreateDeviceReceiver.register(mCreateDeviceOperation);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateOperationStatus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_post_example_submit_device_submit:
                submit();
                break;
        }
    }

    private boolean checkRequiredField(EditText editText) {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            editText.setError(getString(R.string.required));
            editText.requestFocus();
            return false;
        }
        editText.setError(null);
        return true;
    }

    private void submit() {
        if (checkRequiredField(vName) && checkRequiredField(vResolution)) {
                mCreateDeviceOperation.execute(vName.getText().toString(), vResolution.getText().toString());
        }
    }

    private void updateOperationStatus() {
        switch (mCreateDeviceOperation.getStatus()) {
            case READY_TO_EXECUTE:
                vOperationResult.setText("Ready to execute");
                break;
            case WAITING_EXECUTION:
                vOperationResult.setText("Waiting execution");
                break;
            case DOING_EXECUTION:
                vOperationResult.setText("Doing execution");
                break;
            case FINISHED_EXECUTION:
                vOperationResult.setText("Finished execution");
                break;
            case UNKNOWN:
            default:
                vOperationResult.setText("Some error");
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Guardo los datos necesario de la operacion
        mCreateDeviceOperation.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Desregistro el receiver
        mCreateDeviceReceiver.unRegister();
    }
}