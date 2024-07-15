package com.iliauni.idpsyncservice.exception;

public class ModelNotFoundException extends RuntimeException {
    public ModelNotFoundException(String message) {
        super(message);
    }

    public ModelNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelNotFoundException(Throwable cause) {
        super(cause);
    }
}
