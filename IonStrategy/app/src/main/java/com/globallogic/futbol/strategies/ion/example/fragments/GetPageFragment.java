package com.globallogic.futbol.strategies.ion.example.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globallogic.futbol.strategies.ion.example.R;

/**
 * Created by Ezequiel Sanz on 11/05/15.
 * GlobalLogic | ezequiel.sanz@globallogic.com
 */
public class GetPageFragment extends Fragment implements View.OnClickListener {
    public static GetPageFragment newInstance() {
        return new GetPageFragment();
    }

    protected ICallback mCallback;

    public interface ICallback {
        void onExampleGetSuccess();

        void onExampleGetTimeout();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (ICallback.class.isInstance(activity))
            mCallback = (ICallback) activity;
        else
            throw new RuntimeException("The activity " + activity.getClass().getSimpleName() + " must implement " + ICallback.class.getName());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_get_page, container, false);
        rootView.findViewById(R.id.fragment_get_page_success).setOnClickListener(this);
        rootView.findViewById(R.id.fragment_get_page_time_out).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_get_page_success:
                mCallback.onExampleGetSuccess();
                break;
            case R.id.fragment_get_page_time_out:
                mCallback.onExampleGetTimeout();
                break;
        }
    }
}