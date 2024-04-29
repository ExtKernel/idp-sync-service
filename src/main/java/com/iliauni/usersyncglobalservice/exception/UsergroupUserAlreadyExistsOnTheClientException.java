package com.iliauni.usersyncglobalservice.exception;

public class UsergroupUserAlreadyExistsOnTheClientException extends RuntimeException {
    public UsergroupUserAlreadyExistsOnTheClientException(String message) {
        super(message);
    }

    public UsergroupUserAlreadyExistsOnTheClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsergroupUserAlreadyExistsOnTheClientException(Throwable cause) {
        super(cause);
    }
}
