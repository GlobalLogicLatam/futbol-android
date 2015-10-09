package com.globallogic.futbol.example.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.globallogic.futbol.example.R;
import com.globallogic.futbol.example.operations.GetFileOperation;

import java.io.File;

public class GetFileFragment extends Fragment implements GetFileOperation.IGetFileReceiver {
    public static final String TAG = "GetFileFragment";

    private final GetFileOperation mGetFileOperation;
    private final GetFileOperation.GetFileReceiver mGetFileReceiver;
    private TextView vOperationStatus;
    private TextView vOperationResult;

    public GetFileFragment() {
        mGetFileOperation = new GetFileOperation();
        mGetFileReceiver = new GetFileOperation.GetFileReceiver(this);
    }

    public static GetFileFragment newInstance() {
        return new GetFileFragment();
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
    public void onSuccess(File aFile) {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.fromFile(new File(aFile.getAbsolutePath())), "image/*");
        startActivity(i);
    }

    @Override
    public void onError() {
        vOperationResult.setText(mGetFileOperation.getError(getActivity()));
    }

    @Override
    public void onFinishOperation() {
        updateOperationStatus();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGetFileOperation.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_get_example_get_device, container, false);
        vOperationStatus = (TextView) rootView.findViewById(R.id.operation_status);
        vOperationResult = (TextView) rootView.findViewById(R.id.operation_result);

        mGetFileReceiver.register(mGetFileOperation);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mGetFileOperation.execute();
    }

    private void updateOperationStatus() {
        switch (mGetFileOperation.getStatus()) {
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
        mGetFileOperation.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Desregistro el receiver
        mGetFileReceiver.unRegister();
    }
}