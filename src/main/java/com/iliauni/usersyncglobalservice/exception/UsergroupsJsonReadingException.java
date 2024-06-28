package com.iliauni.usersyncglobalservice.exception;

public class UsergroupsJsonReadingException extends RuntimeException {
    public UsergroupsJsonReadingException(String message) {
        super(message);
    }

    public UsergroupsJsonReadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsergroupsJsonReadingException(Throwable cause) {
        super(cause);
    }
}
