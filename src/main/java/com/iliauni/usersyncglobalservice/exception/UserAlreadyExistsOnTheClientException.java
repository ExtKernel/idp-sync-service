package com.iliauni.usersyncglobalservice.exception;

public class UserAlreadyExistsOnTheClientException extends RuntimeException {
    public UserAlreadyExistsOnTheClientException(String message) {
        super(message);
    }

    public UserAlreadyExistsOnTheClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistsOnTheClientException(Throwable cause) {
        super(cause);
    }
}
