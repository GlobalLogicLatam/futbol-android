package com.globallogic.futbol.example.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.globallogic.futbol.example.R;
import com.globallogic.futbol.example.entities.Device;
import com.globallogic.futbol.example.interfaces.IDevicesAdapterCallbacks;

/**
 * Created by facundo.mengoni on 10/5/2015.
 */
public class DeviceViewHolder extends RecyclerView.ViewHolder {

    //region Views
    TextView vTitle;
    private IDevicesAdapterCallbacks mCallback;
    //endregion

    public DeviceViewHolder(View itemView, final IDevicesAdapterCallbacks callback) {
        super(itemView);
        mCallback = callback;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onItemClick(getAdapterPosition());
            }
        });
        vTitle = (TextView) itemView.findViewById(R.id.view_holder_item_title);
    }

    public void load(Device device) {
        vTitle.setText(device.toString());
    }
}