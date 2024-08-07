package com.iliauni.idpsyncservice.exception;

public class UsergroupMemberDoesNotExistOnTheClientException extends RuntimeException {
    public UsergroupMemberDoesNotExistOnTheClientException(String message) {
        super(message);
    }

    public UsergroupMemberDoesNotExistOnTheClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsergroupMemberDoesNotExistOnTheClientException(Throwable cause) {
        super(cause);
    }
}
