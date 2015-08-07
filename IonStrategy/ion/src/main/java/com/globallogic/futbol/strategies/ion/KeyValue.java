package com.globallogic.futbol.strategies.ion;

import java.io.Serializable;

/**
 * Created by Facundo Mengoni on 8/6/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class KeyValue implements Cloneable, Serializable {
    private final String key;
    private final String value;

    public KeyValue(String key, String value) {
        if (key == null)
            throw new RuntimeException("The parameter key can't be null");
        if (value == null)
            throw new RuntimeException("The parameter value can't be null");
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "KeyValue{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyValue keyValue = (KeyValue) o;

        if (!key.equals(keyValue.key)) return false;
        return value.equals(keyValue.value);
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public KeyValue clone() throws CloneNotSupportedException {
        return new KeyValue(key, value);
    }
}