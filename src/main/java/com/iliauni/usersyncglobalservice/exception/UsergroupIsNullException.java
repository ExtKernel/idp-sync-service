package com.iliauni.usersyncglobalservice.exception;

public class UsergroupIsNullException extends RuntimeException {
    public UsergroupIsNullException(String message) {
        super(message);
    }

    public UsergroupIsNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsergroupIsNullException(Throwable cause) {
        super(cause);
    }
}
