package com.iliauni.usersyncglobalservice.exception;

public class UsergroupsDoNotExistOnTheClientException extends RuntimeException {
    public UsergroupsDoNotExistOnTheClientException(String message) {
        super(message);
    }

    public UsergroupsDoNotExistOnTheClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsergroupsDoNotExistOnTheClientException(Throwable cause) {
        super(cause);
    }
}
