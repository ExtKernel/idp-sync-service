package com.iliauni.usersyncglobalservice.exception;

public class UsergroupJsonReadingException extends RuntimeException {
    public UsergroupJsonReadingException(String message) {
        super(message);
    }

    public UsergroupJsonReadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsergroupJsonReadingException(Throwable cause) {
        super(cause);
    }
}
