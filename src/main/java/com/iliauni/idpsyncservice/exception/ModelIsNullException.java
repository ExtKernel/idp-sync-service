package com.iliauni.idpsyncservice.exception;

public class ModelIsNullException extends RuntimeException {
    public ModelIsNullException(String message) {
        super(message);
    }

    public ModelIsNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelIsNullException(Throwable cause) {
        super(cause);
    }
}
