package com.iliauni.usersyncglobalservice.exception;

public class UserToJsonMappingException extends RuntimeException {
    public UserToJsonMappingException(String message) {
        super(message);
    }

    public UserToJsonMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserToJsonMappingException(Throwable cause) {
        super(cause);
    }
}
