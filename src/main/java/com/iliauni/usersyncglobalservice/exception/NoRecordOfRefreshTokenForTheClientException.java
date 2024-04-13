package com.iliauni.usersyncglobalservice.exception;

public class NoRecordOfRefreshTokenForTheClientException extends RuntimeException {
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
