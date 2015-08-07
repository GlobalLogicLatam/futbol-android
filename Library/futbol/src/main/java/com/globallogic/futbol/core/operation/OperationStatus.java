package com.globallogic.futbol.core.operation;

import java.io.Serializable;

public enum OperationStatus implements Serializable {
    UNKNOWN(0),
    READY_TO_EXECUTE(1),
    WAITING_EXECUTION(2),
    DOING_EXECUTION(3),
    FINISHED_EXECUTION(4);

    public int id;

    OperationStatus(int id) {
        this.id = id;
    }

    public static OperationStatus toEnum(int value) {
        switch (value) {
            case 1:
                return READY_TO_EXECUTE;
            case 2:
                return WAITING_EXECUTION;
            case 3:
                return DOING_EXECUTION;
            case 4:
                return FINISHED_EXECUTION;
            case 0:
            default:
                return UNKNOWN;
        }
    }
}
