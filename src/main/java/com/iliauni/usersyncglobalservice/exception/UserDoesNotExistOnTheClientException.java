package com.iliauni.usersyncglobalservice.exception;

public class UserDoesNotExistOnTheClientException extends RuntimeException {
    public UserDoesNotExistOnTheClientException(String message) {
        super(message);
    }

    public UserDoesNotExistOnTheClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDoesNotExistOnTheClientException(Throwable cause) {
        super(cause);
    }
}
