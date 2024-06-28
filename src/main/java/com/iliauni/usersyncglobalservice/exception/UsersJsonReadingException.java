package com.iliauni.usersyncglobalservice.exception;

public class UsersJsonReadingException extends RuntimeException {
    public UsersJsonReadingException(String message) {
        super(message);
    }

    public UsersJsonReadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsersJsonReadingException(Throwable cause) {
        super(cause);
    }
}
