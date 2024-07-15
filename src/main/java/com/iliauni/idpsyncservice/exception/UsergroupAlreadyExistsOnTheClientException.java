package com.iliauni.idpsyncservice.exception;

public class UsergroupAlreadyExistsOnTheClientException extends RuntimeException {
    public UsergroupAlreadyExistsOnTheClientException(String message) {
        super(message);
    }

    public UsergroupAlreadyExistsOnTheClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsergroupAlreadyExistsOnTheClientException(Throwable cause) {
        super(cause);
    }
}
