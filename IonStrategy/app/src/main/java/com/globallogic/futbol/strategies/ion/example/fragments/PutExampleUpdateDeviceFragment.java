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
import com.globallogic.futbol.strategies.ion.example.operations.UpdateDeviceOperation;

/**
 * Created by Ezequiel Sanz on 11/05/15.
 * GlobalLogic | ezequiel.sanz@globallogic.com
 */
public class PutExampleUpdateDeviceFragment extends Fragment implements UpdateDeviceOperation.IUpdateDeviceReceiver, View.OnClickListener {
    public static final String TAG = "PutExampleUpdateDeviceFragment";
    private EditText vId;
    private EditText vName;
    private EditText vResolution;
    private Button vSubmit;
    private TextView vOperationResult;

    // Defino mi operacion y mi receiver
    private final UpdateDeviceOperation mUpdateDeviceOperation;
    private final UpdateDeviceOperation.UpdateDeviceReceiver mUpdateDeviceReceiver;


    public static PutExampleUpdateDeviceFragment newInstance() {
        return new PutExampleUpdateDeviceFragment();
    }

    public PutExampleUpdateDeviceFragment() {
        mUpdateDeviceOperation = new UpdateDeviceOperation();
        mUpdateDeviceReceiver = new UpdateDeviceOperation.UpdateDeviceReceiver(this);
    }

    private void enableButtons(boolean value) {
        vId.setEnabled(value);
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
        vOperationResult.setText(R.string.doing_operation);
        enableButtons(false);
    }

    @Override
    public void onFinishOperation() {
        //Nothing to do
    }

    @Override
    public void onSuccess(Device aDevice) {
        vOperationResult.setText(aDevice.toString());
        enableButtons(true);
    }

    @Override
    public void onError() {
        vOperationResult.setText(mUpdateDeviceOperation.getError(getActivity()));
        enableButtons(true);
    }

    @Override
    public void onNotFound() {
        vOperationResult.setText(getString(R.string.not_found));
        enableButtons(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUpdateDeviceOperation.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_put_example_update_device, container, false);
        vId = (EditText) rootView.findViewById(R.id.fragment_put_example_update_device_id);
        vName = (EditText) rootView.findViewById(R.id.fragment_put_example_update_device_name);
        vResolution = (EditText) rootView.findViewById(R.id.fragment_put_example_update_device_resolution);
        vSubmit = (Button) rootView.findViewById(R.id.fragment_put_example_update_device_submit);
        vSubmit.setOnClickListener(this);
        vOperationResult = (TextView) rootView.findViewById(R.id.fragment_put_example_update_device_result);

        mUpdateDeviceReceiver.startListening(mUpdateDeviceOperation);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_put_example_update_device_submit:
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
        if (checkRequiredField(vId) && checkRequiredField(vName) && checkRequiredField(vResolution)) {
            mUpdateDeviceOperation.execute(vId.getText().toString(), vName.getText().toString(), vResolution.getText().toString());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Guardo los datos necesario de la operacion
        mUpdateDeviceOperation.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Desregistro el receiver
        mUpdateDeviceReceiver.stopListening();
    }
}