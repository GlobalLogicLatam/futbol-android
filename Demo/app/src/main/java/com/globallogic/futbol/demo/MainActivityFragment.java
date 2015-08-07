package com.globallogic.futbol.demo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koushikdutta.ion.Ion;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements DemoOperation.IDemoOperation {

    private final DemoOperation mOperation;
    private final DemoOperation.DemoReceiver mOperationReceiver;

    public MainActivityFragment() {
        mOperation = new DemoOperation();
        mOperationReceiver = new DemoOperation.DemoReceiver(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOperation.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mOperationReceiver.register(mOperation); // <-- Don't forgot register the receiver
        doOperation();
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private void doOperation() {
        String id = "1";
        mOperation.performOperation(id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mOperation.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mOperationReceiver.unRegister(); // <-- Don't forgot unregister the receiver
    }

    @Override
    public void onStartOperation() {
        // ToDo Do something in the UI
    }

    @Override
    public void onSuccess(DemoModel model) {
        // ToDo Do something in the UI
    }

    @Override
    public void onError() {
        // ToDo Do something in the UI
    }

    @Override
    public void onNotFound() {
        // ToDo Do something in the UI
    }
}
