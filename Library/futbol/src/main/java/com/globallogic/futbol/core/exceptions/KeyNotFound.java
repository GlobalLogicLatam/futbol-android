package com.globallogic.futbol.core.exceptions;

/**
 * Thrown when the key doesn't exists in the shared preference.
 *
 * @author facundo.mengoni
 * @since 0.3.3
 */
public class KeyNotFound extends Exception {
    public KeyNotFound() {
    }

    public KeyNotFound(String message) {
        super(message);
    }
}