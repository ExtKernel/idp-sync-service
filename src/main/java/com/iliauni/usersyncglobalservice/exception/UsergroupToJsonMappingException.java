package com.iliauni.usersyncglobalservice.exception;

public class UsergroupToJsonMappingException extends RuntimeException {
    public UsergroupToJsonMappingException(String message) {
        super(message);
    }

    public UsergroupToJsonMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsergroupToJsonMappingException(Throwable cause) {
        super(cause);
    }
}
