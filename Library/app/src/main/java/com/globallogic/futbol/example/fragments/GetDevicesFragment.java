package com.globallogic.futbol.example.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.globallogic.futbol.example.R;
import com.globallogic.futbol.example.activities.GenericExampleActivity;
import com.globallogic.futbol.example.adapters.DevicesAdapter;
import com.globallogic.futbol.example.entities.Device;
import com.globallogic.futbol.example.interfaces.IDevicesAdapterCallbacks;
import com.globallogic.futbol.example.operations.GetDeviceOperation;
import com.globallogic.futbol.example.operations.GetDevicesOperation;

import java.util.ArrayList;

/**
 * Created by Ezequiel Sanz on 11/05/15.
 * GlobalLogic | ezequiel.sanz@globallogic.com
 */
public class GetDevicesFragment extends Fragment {
    public static final String TAG = "GetDevicesFragment";
    private TextView vOperationStatus;
    private TextView vOperationResult;
    private ViewSwitcher vOperationResultContainer;
    private ViewSwitcher vOperationWaitingContainer;
    private RecyclerView vRecyclerView;
    private DevicesAdapter mMediasAdapter;

    //region GetDevicesOperation
    // Defino mi operacion y mi receiver
    private final GetDevicesOperation mGetDevicesOperation;
    private final GetDevicesOperation.GetDevicesReceiver mGetDevicesReceiver;
    private final GetDevicesOperation.IGetDevicesReceiver mGetDevicesCallback = new GetDevicesOperation.IGetDevicesReceiver() {
        @Override
        public void onNoInternet() {
            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStartOperation() {
            if (vOperationWaitingContainer.getDisplayedChild() == 0)
                vOperationWaitingContainer.showNext();
            updateOperationStatus();
        }

        @Override
        public void onSuccess(ArrayList<Device> aList) {
            mMediasAdapter.addList(aList);
            if (vOperationResultContainer.getDisplayedChild() == 0)
                vOperationResultContainer.showNext();
        }

        @Override
        public void onError() {
            vOperationResult.setText(mGetDevicesOperation.getError(getActivity()));
            if (vOperationResultContainer.getDisplayedChild() == 1)
                vOperationResultContainer.showPrevious();
        }

        @Override
        public void onFinishOperation() {
            if (vOperationWaitingContainer.getDisplayedChild() == 1)
                vOperationWaitingContainer.showPrevious();
            updateOperationStatus();
        }

    };
    //endregion
    //region GetDeviceOperation
    // Defino mi receiver
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
            mMediasAdapter.update(aDevice);
            if (vOperationResultContainer.getDisplayedChild() == 0)
                vOperationResultContainer.showNext();
        }

        @Override
        public void onError() {
        }

        @Override
        public void onFinishOperation() {
            if (vOperationWaitingContainer.getDisplayedChild() == 1)
                vOperationWaitingContainer.showPrevious();
        }
    };
    //endregion

    public GetDevicesFragment() {
        String id = "1";
        mGetDevicesOperation = new GetDevicesOperation(id);
        mGetDevicesReceiver = new GetDevicesOperation.GetDevicesReceiver(mGetDevicesCallback);
        mGetDeviceReceiver = new GetDeviceOperation.GetDeviceReceiver(mGetDeviceCallback);
    }

    public static GetDevicesFragment newInstance() {
        return new GetDevicesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGetDevicesOperation.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_get_example_get_list, container, false);
        vOperationStatus = (TextView) rootView.findViewById(R.id.operation_status);
        vOperationResult = (TextView) rootView.findViewById(R.id.operation_result);
        vOperationResultContainer = (ViewSwitcher) rootView.findViewById(R.id.operation_result_container);
        vOperationWaitingContainer = (ViewSwitcher) rootView.findViewById(R.id.operation_waiting_container);
        vRecyclerView = (RecyclerView) rootView.findViewById(R.id.operation_list);
        vRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        mMediasAdapter = new DevicesAdapter(new IDevicesAdapterCallbacks() {
            @Override
            public void onItemClick(int position) {
                startActivity(GenericExampleActivity.generateIntent(getActivity(), GetDeviceFragment.TAG));
            }
        });
        vRecyclerView.setAdapter(mMediasAdapter);

        mGetDevicesReceiver.register(mGetDevicesOperation);
        mGetDeviceReceiver.register(GetDeviceOperation.class);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mGetDevicesOperation.execute();
    }

    private void updateOperationStatus() {
        switch (mGetDevicesOperation.getStatus()) {
            case READY_TO_EXECUTE:
                vOperationStatus.setText("Ready to execute");
                break;
            case WAITING_EXECUTION:
                vOperationStatus.setText("Waiting execution");
                break;
            case DOING_EXECUTION:
                vOperationStatus.setText("Doing execution");
                break;
            case FINISHED_EXECUTION:
                vOperationStatus.setText("Finished execution");
                break;
            case UNKNOWN:
            default:
                vOperationStatus.setText("Some error");
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Guardo los datos necesario de la operacion
        mGetDevicesOperation.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Desregistro el receiver
        mGetDevicesReceiver.unRegister();
        mGetDeviceReceiver.unRegister();
    }
}