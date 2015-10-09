package com.globallogic.futbol.example.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.globallogic.futbol.example.R;
import com.globallogic.futbol.example.operations.TimeOutOperationHttp;

/**
 * Created by Ezequiel Sanz on 11/05/15.
 * GlobalLogic | ezequiel.sanz@globallogic.com
 */
public class GetExampleTimeOutFragment extends Fragment implements TimeOutOperationHttp.ITimeOutReceiver {
    public static final String TAG = "GetExampleTimeOutFragment";
    // Defino mi operacion y mi receiver
    private final TimeOutOperationHttp mTimeOutOperation;
    private final TimeOutOperationHttp.TimeOutReceiver mTimeOutReceiver;
    private TextView vOperationStatus;
    private TextView vOperationResult;

    public GetExampleTimeOutFragment() {
        mTimeOutOperation = new TimeOutOperationHttp();
        mTimeOutReceiver = new TimeOutOperationHttp.TimeOutReceiver(this);
    }

    public static GetExampleTimeOutFragment newInstance() {
        return new GetExampleTimeOutFragment();
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
    public void onSuccess() {
        updateOperationStatus();
        vOperationResult.setText("Success");
    }

    @Override
    public void onError() {
        updateOperationStatus();
        vOperationResult.setText(mTimeOutOperation.getError(getActivity()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTimeOutOperation.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_get_example_get_device, container, false);
        vOperationStatus = (TextView) rootView.findViewById(R.id.operation_status);
        vOperationResult = (TextView) rootView.findViewById(R.id.operation_result);

        mTimeOutReceiver.register(mTimeOutOperation);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        submit();
    }

    private void submit() {
        mTimeOutOperation.execute();
    }

    private void updateOperationStatus() {
        switch (mTimeOutOperation.getStatus()) {
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
        mTimeOutOperation.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Desregistro el receiver
        mTimeOutReceiver.unRegister();
    }
}