package com.globallogic.futbol.example.presenters;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.globallogic.futbol.example.R;
import com.globallogic.futbol.example.adapters.DevicesAdapter;
import com.globallogic.futbol.example.domain.models.Device;
import com.globallogic.futbol.example.domain.operations.GetDeviceOperation;
import com.globallogic.futbol.example.domain.operations.GetDevicesOperation;
import com.globallogic.futbol.example.interfaces.IDevicesAdapterCallbacks;

import java.util.ArrayList;

/**
 * @author facundo.mengoni
 * @since 0.3
 */
public class GetDevicesPresenter {
    //region Constants
    private static final String SAVED_INSTANCE_HAS_HTTP_RESPONSE = "SAVED_INSTANCE_HAS_HTTP_RESPONSE";
    private static final String SAVED_INSTANCE_HAS_DB_RESPONSE = "SAVED_INSTANCE_HAS_DB_RESPONSE";
    //endregion

    private final IGetDevicesView view;

    //region Presenter vars
    private boolean hasHttpResponse;
    private boolean hasDbResponse;

    private DevicesAdapter mDevicesAdapter;

    //region Operation vars
    //region GetDevicesOperation vars
    // Defino mi operacion y mi receiver
    private final GetDevicesOperation mGetDevicesOperation;
    private final GetDevicesOperation.GetDevicesReceiver mGetDevicesReceiver;
    private final GetDevicesOperation.IGetDevicesReceiver mGetDevicesCallback = new GetDevicesOperation.IGetDevicesReceiver() {
        @Override
        public void onNoInternet() {
            Toast.makeText(view.getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStartOperation() {
            if (!hasDbResponse && !hasHttpResponse)
                view.changeWaitingView(true);
        }

        @Override
        public void onSuccessHttp(final ArrayList<Device> aList) {
            hasHttpResponse = true;
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDevicesAdapter.addList(aList);
                    view.changeWaitingView(false);
                    view.changeNormalView(false);
                }
            };
            if (hasDbResponse) {
                view.showSnackBar("Hay nuevos elementos", Snackbar.LENGTH_LONG, "Ver", onClickListener);
            } else {
                onClickListener.onClick(null);
            }
        }

        @Override
        public void onSuccessDb(ArrayList<Device> aList) {
            hasDbResponse = true;
            if (!hasHttpResponse) {
                mDevicesAdapter.addList(aList);
                view.changeWaitingView(false);
                view.changeNormalView(false);
            }
        }

        @Override
        public void onError() {
            view.changeWaitingView(false);
            view.changeNormalView(true);
            view.setErrorMessage(mGetDevicesOperation.getError(view.getActivity()));
        }

        @Override
        public void onFinishOperation() {
            if (hasDbResponse || hasHttpResponse)
                view.changeWaitingView(false);
        }
    };
    //endregion
    //region GetDeviceOperation vars
    // Defino sólo mi receiver
    private final GetDeviceOperation.GetDeviceReceiver mGetDeviceReceiver;
    private final GetDeviceOperation.IGetDeviceReceiver mGetDeviceCallback = new GetDeviceOperation.IGetDeviceReceiver() {
        @Override
        public void onNoInternet() {
        }

        @Override
        public void onStartOperation() {
        }

        @Override
        public void onSuccess(Device aDevice) {
            if (hasDbResponse || hasHttpResponse) {
                mDevicesAdapter.update(aDevice);
            }
        }

        @Override
        public void onError() {
        }

        @Override
        public void onFinishOperation() {
        }
    };
    //endregion
    //endregion
    //endregion

    public GetDevicesPresenter(IGetDevicesView view) {
        this.view = view;
        mGetDevicesOperation = new GetDevicesOperation();
        mGetDevicesReceiver = new GetDevicesOperation.GetDevicesReceiver(mGetDevicesCallback);
        mGetDeviceReceiver = new GetDeviceOperation.GetDeviceReceiver(mGetDeviceCallback);
    }

    //region Lifecycle implementation
    public void onCreate(Bundle savedInstanceState) {
        mDevicesAdapter = new DevicesAdapter(new IDevicesAdapterCallbacks() {
            @Override
            public void onItemClick(int position) {
                view.goToDeviceDetail(mDevicesAdapter.getDevice(position));
            }
        });
        mGetDevicesReceiver.startListening(mGetDevicesOperation);
        mGetDeviceReceiver.startListening(GetDeviceOperation.class);
        mGetDevicesOperation.onCreate(savedInstanceState);
    }

    public void onSaveInstanceState(Bundle outState) {
        mDevicesAdapter.onSaveInstanceState(outState);
        mGetDevicesOperation.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_INSTANCE_HAS_HTTP_RESPONSE, hasHttpResponse);
        outState.putBoolean(SAVED_INSTANCE_HAS_DB_RESPONSE, hasDbResponse);
    }

    private void onRestoreInstance(Bundle savedInstanceState) {
        mDevicesAdapter.onRestoreInstance(savedInstanceState);
        hasHttpResponse = savedInstanceState.getBoolean(SAVED_INSTANCE_HAS_HTTP_RESPONSE);
        hasDbResponse = savedInstanceState.getBoolean(SAVED_INSTANCE_HAS_DB_RESPONSE);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            onRestoreInstance(savedInstanceState);
            if (mDevicesAdapter.getItemCount() > 0) {
                view.changeNormalView(false);
            }
        } else {
            mGetDevicesOperation.execute();
        }
    }

    public void onDestroy() {
        mGetDevicesReceiver.stopListening();
        mGetDeviceReceiver.stopListening();
    }
    //endregion

    //region Public methods implementation
    public RecyclerView.Adapter getMediasAdapter() {
        return mDevicesAdapter;
    }
    //endregion
}