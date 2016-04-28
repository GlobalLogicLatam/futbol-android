package com.globallogic.futbol.demo.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.globallogic.futbol.demo.R;
import com.globallogic.futbol.demo.app.operations.CreateDeviceOperation;
import com.globallogic.futbol.demo.domain.Device;


public class DetailActivity extends ActionBarActivity implements CreateDeviceOperation.ICreateDeviceOperation {

    private EditText vName;
    private EditText vResolution;
    private Button vSubmit;

    private final CreateDeviceOperation mCreateDeviceOperation;
    private final CreateDeviceOperation.CreateDeviceReceiver mCreateDeviceReceiver;


    public static Intent generateIntent(Context aContext) {
        return new Intent(aContext, DetailActivity.class);
    }

    public DetailActivity() {
        mCreateDeviceOperation = new CreateDeviceOperation();
        mCreateDeviceReceiver = new CreateDeviceOperation.CreateDeviceReceiver(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar vToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(vToolbar);

        mCreateDeviceReceiver.startListening(mCreateDeviceOperation);
        vName = (EditText) findViewById(R.id.activity_detail_name);
        vResolution = (EditText) findViewById(R.id.activity_detail_resolution);
        vSubmit = (Button) findViewById(R.id.activity_detail_submit);
        vSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCreateDeviceOperation.performOperation(vName.getText().toString(), vResolution.getText().toString());
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCreateDeviceOperation.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCreateDeviceOperation.onRestoreSavedInstance(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCreateDeviceReceiver.stopListening();
    }

    @Override
    public void onStartOperation() {
        vName.setEnabled(false);
        vResolution.setEnabled(false);
        vSubmit.setEnabled(false);
    }

    @Override
    public void onFinishOperation() {

    }

    @Override
    public void onSuccess(Device device) {
        Toast.makeText(this, "Se creó correctamente", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(@StringRes int resError) {
        vName.setEnabled(true);
        vResolution.setEnabled(true);
        vSubmit.setEnabled(true);
        Toast.makeText(this, getString(resError), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoInternet() {

    }
}