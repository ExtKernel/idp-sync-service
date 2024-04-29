package com.iliauni.usersyncglobalservice.exception;

public class UsergroupDoesNotExistOnTheClientException extends RuntimeException {
    public UsergroupDoesNotExistOnTheClientException(String message) {
        super(message);
    }

    public UsergroupDoesNotExistOnTheClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsergroupDoesNotExistOnTheClientException(Throwable cause) {
        super(cause);
    }
}
