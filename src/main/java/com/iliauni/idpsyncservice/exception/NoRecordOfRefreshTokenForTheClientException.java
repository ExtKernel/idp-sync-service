package com.iliauni.idpsyncservice.exception;

public class NoRecordOfRefreshTokenForTheClientException extends Exception {
    public NoRecordOfRefreshTokenForTheClientException(String message) {
        super(message);
    }

    public NoRecordOfRefreshTokenForTheClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoRecordOfRefreshTokenForTheClientException(Throwable cause) {
        super(cause);
    }
}
