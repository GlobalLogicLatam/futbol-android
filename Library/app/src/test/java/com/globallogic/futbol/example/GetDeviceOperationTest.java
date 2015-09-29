package com.globallogic.futbol.example;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.globallogic.futbol.core.OperationApp;
import com.globallogic.futbol.core.operation.OperationStatus;
import com.globallogic.futbol.core.operation.strategies.StrategyMockResponse;
import com.globallogic.futbol.example.entities.Device;
import com.globallogic.futbol.example.operations.GetDeviceOperation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowNetworkInfo;

import java.net.HttpURLConnection;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Facundo Mengoni on 22/9/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class GetDeviceOperationTest {
    public static final String TAG = GetDeviceOperationTest.class.getSimpleName();

    //region Logger
    public static Logger mLogger;

    static {
        mLogger = Logger.getLogger(TAG);
        mLogger.setLevel(Level.ALL);
    }
    //endregion

    private void setConnectionAs(NetworkInfo.DetailedState disconnected, int typeWifi, int subType, boolean isConnected) {
        ConnectivityManager connectivityManager = (ConnectivityManager) OperationApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = ShadowNetworkInfo.newInstance(disconnected, typeWifi, subType, true, isConnected);
        Shadows.shadowOf(connectivityManager).setActiveNetworkInfo(wifi);
    }

    private boolean hasInternet() {
        ConnectivityManager cm = (ConnectivityManager) OperationApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return Boolean.FALSE;
        }
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) {
            return Boolean.FALSE;
        }
        return networkInfo.isConnected();
    }

    @Before
    public void setUp() {
    }

    private Context getContext() {
        return RuntimeEnvironment.application;
    }

    @Test
    public void getDeviceTimeout() {
        final String id = "1";
        //region Setup
        final GetDeviceOperation mGetDeviceTimeOutOperation = new GetDeviceOperation(id);
        final GetDeviceOperation.IGetDeviceReceiver mGetDeviceTimeOutCallback = new GetDeviceOperation.IGetDeviceReceiver() {
            @Override
            public void onSuccess(Device aDevice) {
                assertTrue("This should never happen", false);
            }

            @Override
            public void onError() {
                String expectedError = getContext().getString(R.string.error_time_out_exception);
                assertTrue(String.format("The error was \"%s\" and was expected \"%s\"",
                                mGetDeviceTimeOutOperation.getError(getContext()),
                                expectedError),
                        mGetDeviceTimeOutOperation.getError(getContext()).equals(expectedError));
            }

            @Override
            public void onNoInternet() {
                assertTrue("This should never happen", false);
            }

            @Override
            public void onStartOperation() {
            }
        };
        final GetDeviceOperation.GetDeviceReceiver mGetDeviceTimeOutReceiver = new GetDeviceOperation.GetDeviceReceiver(mGetDeviceTimeOutCallback);
        //endregion

        //region Register
        mGetDeviceTimeOutReceiver.register(mGetDeviceTimeOutOperation);
        //endregion

        //region Test
        mGetDeviceTimeOutOperation.testResponse(new TimeoutException());
        assertTrue("The operation is finished", mGetDeviceTimeOutOperation.getStatus() == OperationStatus.FINISHED_EXECUTION);
        //endregion
    }

    @Test
    public void getDeviceSuccess() {
        final String id = "2";
        //region Setup
        final GetDeviceOperation mGetDeviceSuccessOperation = new GetDeviceOperation(id);
        final GetDeviceOperation.IGetDeviceReceiver mGetDeviceSuccessCallback = new GetDeviceOperation.IGetDeviceReceiver() {
            @Override
            public void onSuccess(Device aDevice) {
                assertTrue(String.format("The id was \"%s\" and was expected \"%s\"",
                                aDevice.getId(),
                                id),
                        id.equals(aDevice.getId()));
                assertTrue(String.format("The id was \"%s\" and was expected \"%s\"",
                                aDevice.getName(),
                                "S3"),
                        "S3".equals(aDevice.getName()));
                assertTrue(String.format("The id was \"%s\" and was expected \"%s\"",
                                aDevice.getResolution(),
                                "720x1280"),
                        "720x1280".equals(aDevice.getResolution()));
            }

            @Override
            public void onError() {
                assertTrue("This should never happen", false);
            }

            @Override
            public void onNoInternet() {
                assertTrue("This should never happen", false);
            }

            @Override
            public void onStartOperation() {
            }
        };
        final GetDeviceOperation.GetDeviceReceiver mGetDeviceSuccessReceiver = new GetDeviceOperation.GetDeviceReceiver(mGetDeviceSuccessCallback);
        //endregion

        //region Register
        mGetDeviceSuccessReceiver.register(mGetDeviceSuccessOperation);
        //endregion

        //region Test
        mGetDeviceSuccessOperation.testResponse(new StrategyMockResponse(HttpURLConnection.HTTP_OK, "{\"createdAt\":\"2015-08-05T11:14:45.374Z\",\"id\":\"" + id + "\",\"name\":\"S3\",\"resolution\":\"720x1280\",\"updatedAt\":\"2015-08-05T11:14:45.374Z\"}"));
        assertTrue(mGetDeviceSuccessOperation.getStatus() == OperationStatus.FINISHED_EXECUTION);
        //endregion
    }

    @Test
    public void getDeviceNoInternet() {
        final String id = "3";
        //region Setup
        setConnectionAs(NetworkInfo.DetailedState.DISCONNECTED, ConnectivityManager.TYPE_WIFI, 0, false);
        final GetDeviceOperation mGetDeviceSuccessOperation = new GetDeviceOperation(id);
        final GetDeviceOperation.IGetDeviceReceiver mGetDeviceSuccessCallback = new GetDeviceOperation.IGetDeviceReceiver() {
            @Override
            public void onSuccess(Device aDevice) {
                assertTrue("This should never happen", false);
            }

            @Override
            public void onError() {
                assertTrue("This should never happen", false);
            }

            @Override
            public void onNoInternet() {
                assertTrue("It was expected that no internet", !hasInternet());
            }

            @Override
            public void onStartOperation() {
            }
        };
        final GetDeviceOperation.GetDeviceReceiver mGetDeviceSuccessReceiver = new GetDeviceOperation.GetDeviceReceiver(mGetDeviceSuccessCallback);
        //endregion

        //region Register
        mGetDeviceSuccessReceiver.register(mGetDeviceSuccessOperation);
        //endregion

        //region Test
        mGetDeviceSuccessOperation.testResponse(new StrategyMockResponse(HttpURLConnection.HTTP_OK, "{\"createdAt\":\"2015-08-05T11:14:45.374Z\",\"id\":\"" + id + "\",\"name\":\"S3\",\"resolution\":\"720x1280\",\"updatedAt\":\"2015-08-05T11:14:45.374Z\"}"));
        assertTrue(mGetDeviceSuccessOperation.getStatus() == OperationStatus.UNKNOWN || mGetDeviceSuccessOperation.getStatus() == OperationStatus.READY_TO_EXECUTE || mGetDeviceSuccessOperation.getStatus() == OperationStatus.WAITING_EXECUTION);
        //endregion
    }
}