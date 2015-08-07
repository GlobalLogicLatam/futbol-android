package com.globallogic.futbol.strategies.ion;

import java.io.Serializable;

/**
 * Created by Facundo Mengoni on 5/5/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class StrategyIonConfig implements Serializable {
    private final int timeOutMillisecond;

    public StrategyIonConfig(int timeOutMillisecond) {
        this.timeOutMillisecond = timeOutMillisecond;
    }

    public StrategyIonConfig(int min, int seg) {
        this.timeOutMillisecond = calculateMilliseconds(min, seg);
    }

    public int getTimeOutMillisecond() {
        return timeOutMillisecond;
    }

    public static StrategyIonConfig getDefaultConfig() {
        int min = 0;
        int seg = 40;
        StrategyIonConfig strategyIonConfig = new StrategyIonConfig(calculateMilliseconds(min, seg));
        return strategyIonConfig;
    }

    private static int calculateMilliseconds(int min, int seg) {
        return (min * 60 + seg) * 1000;
    }
}