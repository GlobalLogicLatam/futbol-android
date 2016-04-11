package com.globallogic.futbol.example.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.globallogic.futbol.example.R;
import com.globallogic.futbol.example.domain.operations.TimeOutOperation;

/**
 * Created by Ezequiel Sanz on 11/05/15.
 * GlobalLogic | ezequiel.sanz@globallogic.com
 */
public class GetExampleTimeOutFragment extends Fragment implements TimeOutOperation.ITimeOutReceiver {
    public static final String TAG = "GetExampleTimeOutFragment";
    // Defino mi operacion y mi receiver
    private final TimeOutOperation mTimeOutOperation;
    private final TimeOutOperation.TimeOutReceiver mTimeOutReceiver;
    private TextView vOperationStatus;
    private TextView vOperationResult;

    public GetExampleTimeOutFragment() {
        mTimeOutOperation = new TimeOutOperation();
        mTimeOutReceiver = new TimeOutOperation.TimeOutReceiver(this);
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
    }

    @Override
    public void onSuccess() {
        vOperationResult.setText("Success");
    }

    @Override
    public void onError() {
        vOperationResult.setText(mTimeOutOperation.getError(getActivity()));
    }

    @Override
    public void onFinishOperation() {
        //Nothing to do
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_get_example_get_device, container, false);
        vOperationStatus = (TextView) rootView.findViewById(R.id.operation_status);
        vOperationResult = (TextView) rootView.findViewById(R.id.operation_result);

        mTimeOutReceiver.startListening(mTimeOutOperation);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Desregistro el receiver
        mTimeOutReceiver.stopListening();
    }
}