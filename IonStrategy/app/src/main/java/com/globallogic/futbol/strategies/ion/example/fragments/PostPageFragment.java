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
public class PostPageFragment extends Fragment implements View.OnClickListener {
    public static PostPageFragment newInstance() {
        return new PostPageFragment();
    }

    protected ICallback mCallback;

    public interface ICallback {
        void onExamplePostSingleString();
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
        View rootView = inflater.inflate(R.layout.fragment_post_page, container, false);
        rootView.findViewById(R.id.fragment_post_page_submit_item).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_post_page_submit_item:
                mCallback.onExamplePostSingleString();
                break;
        }
    }
}