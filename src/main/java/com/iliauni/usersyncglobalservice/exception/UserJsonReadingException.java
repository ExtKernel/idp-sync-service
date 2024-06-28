package com.iliauni.usersyncglobalservice.exception;

public class UserJsonReadingException extends RuntimeException {
    public UserJsonReadingException(String message) {
        super(message);
    }

    public UserJsonReadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserJsonReadingException(Throwable cause) {
        super(cause);
    }
}
