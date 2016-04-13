package com.globallogic.futbol.core;

import java.io.Serializable;

/**
 * A list of expected results that can be happening with an operation.
 *
 * @author facundo.mengoni
 * @since 0.1.0
 */
public enum OperationResult implements Serializable {
    NO_INTERNET("NO_INTERNET"), START("START"), OK("OK"), ERROR("ERROR"), FINISH("FINISH");
    public static final String EXTRA_OPERATION_RESULT = "EXTRA_OPERATION_RESULT";
    public String name;

    OperationResult(String name) {
        this.name = name;
    }
}
