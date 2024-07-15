package com.iliauni.idpsyncservice.exception;

public class ClientIsNullException extends RuntimeException {
    public ClientIsNullException(String message) {
        super(message);
    }

    public ClientIsNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientIsNullException(Throwable cause) {
        super(cause);
    }
}
