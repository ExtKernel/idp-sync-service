package com.iliauni.idpsyncservice.exception;

public class UserIsNullException extends RuntimeException {
    public UserIsNullException(String message) {
        super(message);
    }

    public UserIsNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserIsNullException(Throwable cause) {
        super(cause);
    }
}
