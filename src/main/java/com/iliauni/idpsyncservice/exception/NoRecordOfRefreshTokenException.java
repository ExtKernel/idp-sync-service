package com.iliauni.idpsyncservice.exception;

public class NoRecordOfRefreshTokenException extends Exception {
    public NoRecordOfRefreshTokenException(String message) {
        super(message);
    }

    public NoRecordOfRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoRecordOfRefreshTokenException(Throwable cause) {
        super(cause);
    }
}
