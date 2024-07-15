package com.iliauni.idpsyncservice.exception;

public class UsergroupMemberAlreadyExistsOnTheClientException extends RuntimeException {
    public UsergroupMemberAlreadyExistsOnTheClientException(String message) {
        super(message);
    }

    public UsergroupMemberAlreadyExistsOnTheClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsergroupMemberAlreadyExistsOnTheClientException(Throwable cause) {
        super(cause);
    }
}
