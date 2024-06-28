package com.iliauni.usersyncglobalservice.exception;

public class NullTokenException extends Exception {
    public NullTokenException(String message) {
        super(message);
    }

    public NullTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullTokenException(Throwable cause) {
        super(cause);
    }
}
