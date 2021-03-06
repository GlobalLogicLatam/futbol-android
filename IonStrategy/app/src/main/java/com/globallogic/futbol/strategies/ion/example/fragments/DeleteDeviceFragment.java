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
import com.globallogic.futbol.strategies.ion.example.operations.DeleteDeviceOperation;

/**
 * Created by Ezequiel Sanz on 11/05/15.
 * GlobalLogic | ezequiel.sanz@globallogic.com
 */
public class DeleteDeviceFragment extends Fragment implements DeleteDeviceOperation.IDeleteDeviceReceiver, View.OnClickListener {
    public static final String TAG = "DeleteDeviceFragment";
    private EditText vId;
    private Button vSubmit;
    private TextView vOperationResult;

    // Defino mi operacion y mi receiver
    private final DeleteDeviceOperation mDeleteDeviceOperation;
    private final DeleteDeviceOperation.DeleteDeviceReceiver mDeleteDeviceReceiver;


    public static DeleteDeviceFragment newInstance() {
        return new DeleteDeviceFragment();
    }

    public DeleteDeviceFragment() {
        mDeleteDeviceOperation = new DeleteDeviceOperation();
        mDeleteDeviceReceiver = new DeleteDeviceOperation.DeleteDeviceReceiver(this);
    }

    private void enableButtons(boolean value) {
        vId.setEnabled(value);
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
        vOperationResult.setText(mDeleteDeviceOperation.getError(getActivity()));
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
        mDeleteDeviceOperation.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_delete_example_delete_device, container, false);
        vId = (EditText) rootView.findViewById(R.id.fragment_delete_example_erase_device_id);
        vSubmit = (Button) rootView.findViewById(R.id.fragment_delete_example_erase_device_submit);
        vSubmit.setOnClickListener(this);
        vOperationResult = (TextView) rootView.findViewById(R.id.fragment_delete_example_erase_device_result);

        mDeleteDeviceReceiver.startListening(mDeleteDeviceOperation);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_delete_example_erase_device_submit:
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
        if (checkRequiredField(vId)) {
                mDeleteDeviceOperation.execute(vId.getText().toString());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Guardo los datos necesario de la operacion
        mDeleteDeviceOperation.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Desregistro el receiver
        mDeleteDeviceReceiver.stopListening();
    }
}