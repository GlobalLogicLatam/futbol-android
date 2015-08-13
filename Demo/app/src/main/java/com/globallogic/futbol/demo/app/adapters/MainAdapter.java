package com.globallogic.futbol.demo.app.adapters;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.globallogic.futbol.demo.R;
import com.globallogic.futbol.demo.domain.Device;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Facundo Mengoni on 2015-08-11.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    public static final String SAVED_INSTANCE_LIST = "SAVED_INSTANCE_LIST" + MainAdapter.class.getSimpleName();
    private ArrayList<Device> mList = new ArrayList<>();
    private ICallback mCallback;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View vLayout;
        public TextView vName;
        public TextView vResolution;
        private Device mDevice;

        public ViewHolder(View v) {
            super(v);
            vLayout = v;
            vName = (TextView) v.findViewById(R.id.item_device_name);
            vResolution = (TextView) v.findViewById(R.id.item_device_resolution);
            vLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCallback != null)
                        mCallback.onDeviceClick(mDevice);
                }
            });
        }

        public void load(Device device) {
            this.mDevice = device;
            this.vName.setText(device.getName());
            this.vResolution.setText(device.getResolution());
        }
    }

    public interface ICallback {
        void onDeviceClick(Device device);
    }

    public MainAdapter() {
    }

    public MainAdapter(ICallback callback) {
        setCallback(callback);
    }

    public void setCallback(ICallback callback) {
        this.mCallback = callback;
    }

    public void onRestoreInstance(Bundle savedInstanceState) {
        mList = (ArrayList<Device>) savedInstanceState.getSerializable(SAVED_INSTANCE_LIST);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SAVED_INSTANCE_LIST, mList);
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Device device = mList.get(position);
        holder.load(device);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addItem(Device aDevice) {
        if (!mList.contains(aDevice)) {
            mList.add(aDevice);
            //notifyItemInserted(mList.indexOf(aDevice));
            notifyDataSetChanged();
        }
    }

    public void addItems(Collection<Device> items) {
        mList.addAll(items);
        notifyDataSetChanged();
    }
}