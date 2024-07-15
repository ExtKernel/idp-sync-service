package com.iliauni.idpsyncservice.exception;

public class RefreshTokenIsNullException extends RuntimeException {
    public RefreshTokenIsNullException(String message) {
        super(message);
    }

    public RefreshTokenIsNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public RefreshTokenIsNullException(Throwable cause) {
        super(cause);
    }
}
