package com.globallogic.futbol.demo.app.operations;

import android.content.Context;
import android.content.Intent;

import com.globallogic.futbol.core.operations.Operation;
import com.globallogic.futbol.core.operations.OperationHelper;
import com.globallogic.futbol.demo.R;

/**
 * Created by Facundo Mengoni on 2015-08-12.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public abstract class MyOperation extends Operation {

    public MyOperation() {
        setConnectionDelay(3 * 1000);
    }
}