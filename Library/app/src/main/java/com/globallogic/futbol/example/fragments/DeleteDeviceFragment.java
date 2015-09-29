package com.globallogic.futbol.example.fragments;

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

import com.globallogic.futbol.example.R;
import com.globallogic.futbol.example.entities.Device;
import com.globallogic.futbol.example.operations.DeleteDeviceOperation;

/**
 * Created by Ezequiel Sanz on 11/05/15.
 * GlobalLogic | ezequiel.sanz@globallogic.com
 */
public class DeleteDeviceFragment extends Fragment implements DeleteDeviceOperation.IDeleteDeviceReceiver, View.OnClickListener {
    public static final String TAG = "DeleteDeviceFragment";
    // Defino mi operacion y mi receiver
    private final DeleteDeviceOperation mDeleteDeviceOperation;
    private final DeleteDeviceOperation.DeleteDeviceReceiver mDeleteDeviceReceiver;
    private EditText vId;
    private Button vSubmit;
    private TextView vOperationResult;


    public DeleteDeviceFragment() {
        mDeleteDeviceOperation = new DeleteDeviceOperation();
        mDeleteDeviceReceiver = new DeleteDeviceOperation.DeleteDeviceReceiver(this);
    }

    public static DeleteDeviceFragment newInstance() {
        return new DeleteDeviceFragment();
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

        mDeleteDeviceReceiver.register(mDeleteDeviceOperation);

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
            mDeleteDeviceOperation.performOperation(vId.getText().toString());
        }
    }

    private void updateOperationStatus() {
        switch (mDeleteDeviceOperation.getStatus()) {
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
        mDeleteDeviceOperation.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Desregistro el receiver
        mDeleteDeviceReceiver.unRegister();
    }
}