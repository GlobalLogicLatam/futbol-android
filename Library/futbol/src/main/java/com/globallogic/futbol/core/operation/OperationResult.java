package com.globallogic.futbol.core.operation;

import java.io.Serializable;

public enum OperationResult implements Serializable {
    NO_INTERNET("NO_INTERNET"), START("START"), OK("OK"), ERROR("ERROR"), FINISH("FINISH");
    public static final String EXTRA_STATUS = "EXTRA_STATUS";
    public String name;

    OperationResult(String name) {
        this.name = name;
    }
}
