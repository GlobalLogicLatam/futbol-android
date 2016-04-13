package com.globallogic.futbol.example.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.globallogic.futbol.example.R;
import com.globallogic.futbol.example.fragments.CreateDeviceFragment;
import com.globallogic.futbol.example.fragments.DeleteDeviceFragment;
import com.globallogic.futbol.example.fragments.GetDeviceFragment;
import com.globallogic.futbol.example.fragments.GetDevicesFragment;
import com.globallogic.futbol.example.fragments.GetExampleTimeOutFragment;
import com.globallogic.futbol.example.fragments.PutExampleUpdateDeviceFragment;

public class GenericExampleActivity extends Activity {

    public static final String EXTRA_TAG = "EXTRA_TAG";
    public static final String EXTRA_ID = "EXTRA_ID";

    public static Intent generateIntent(Context aContext, String aFragmentTag) {
        return generateIntent(aContext, aFragmentTag, null);
    }

    public static Intent generateIntent(Context aContext, String aFragmentTag, Integer anId) {
        Intent intent = new Intent(aContext, GenericExampleActivity.class);
        intent.putExtra(EXTRA_TAG, aFragmentTag);
        intent.putExtra(EXTRA_ID, anId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_example);

        if (savedInstanceState == null) {
            String tag = getIntent().getStringExtra(EXTRA_TAG);
            Integer id = getIntent().getIntExtra(EXTRA_ID, 0);
            Fragment fragment = null;
            switch (tag) {
                case GetDevicesFragment.TAG:
                    fragment = GetDevicesFragment.newInstance();
                    break;
                case GetDeviceFragment.TAG:
                    fragment = GetDeviceFragment.newInstance(id);
                    break;
                case GetExampleTimeOutFragment.TAG:
                    fragment = GetExampleTimeOutFragment.newInstance();
                    break;
                case CreateDeviceFragment.TAG:
                    fragment = CreateDeviceFragment.newInstance();
                    break;
                case DeleteDeviceFragment.TAG:
                    fragment = DeleteDeviceFragment.newInstance();
                    break;
                case PutExampleUpdateDeviceFragment.TAG:
                    fragment = PutExampleUpdateDeviceFragment.newInstance();
                    break;
            }
            if (fragment != null)
                getFragmentManager().beginTransaction().replace(R.id.generic_example_container, fragment, tag).commit();
        }
    }
}