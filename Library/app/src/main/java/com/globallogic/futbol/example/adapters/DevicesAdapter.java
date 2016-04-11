package com.globallogic.futbol.example.adapters;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.globallogic.futbol.example.BuildConfig;
import com.globallogic.futbol.example.R;
import com.globallogic.futbol.example.domain.models.Device;
import com.globallogic.futbol.example.interfaces.IDevicesAdapterCallbacks;
import com.globallogic.futbol.example.viewholders.DeviceViewHolder;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author facundo.mengoni
 */
public class DevicesAdapter extends RecyclerView.Adapter<DeviceViewHolder> implements IDevicesAdapterCallbacks {
    //region Constants
    public static final String TAG = DevicesAdapter.class.getSimpleName();
    private static final String SAVED_INSTANCE_LIST = "SAVED_INSTANCE_LIST";
    //endregion

    //region Variables
    private IDevicesAdapterCallbacks mCallback;
    private ArrayList<Device> mList = new ArrayList<>();
    //endregion

    //region Logger
    public static Logger mLogger;

    static {
        mLogger = Logger.getLogger(TAG);
        if (BuildConfig.DEBUG)
            mLogger.setLevel(Level.ALL);
        else
            mLogger.setLevel(Level.OFF);
    }
    //endregion

    //region LifeCycle
    public DevicesAdapter(IDevicesAdapterCallbacks callback) {
        mCallback = callback;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DeviceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_item, parent, false), this);
    }

    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        holder.load(getDevice(position));
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SAVED_INSTANCE_LIST, mList);
    }

    @SuppressWarnings("unchecked")
    public void onRestoreInstance(Bundle savedInstanceState) {
        mList = (ArrayList<Device>) savedInstanceState.getSerializable(SAVED_INSTANCE_LIST);
    }

    @Override
    public long getItemId(int position) {
        return getDevice(position).getId();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    //endregion

    //region Private API
    // endregion

    //region IDevicesAdapterCallbacks
    @Override
    public void onItemClick(int position) {
        mCallback.onItemClick(position);
    }
    // endregion

    //region Public API
    public void addList(ArrayList<Device> aList) {
        for (Device device : aList) {
            update(device);
        }
    }

    public void update(Device aDevice) {
        int index = mList.indexOf(aDevice);
        if (index >= 0) {
            mList.set(index, aDevice);
            notifyItemChanged(index);
        } else {
            mList.add(aDevice);
            index = mList.indexOf(aDevice);
            notifyItemInserted(index);
        }
    }

    public Device getDevice(int aPosition) {
        return mList.get(aPosition);
    }
    // endregion
}