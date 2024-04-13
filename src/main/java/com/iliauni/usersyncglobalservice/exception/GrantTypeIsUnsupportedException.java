package com.iliauni.usersyncglobalservice.exception;

public class GrantTypeIsUnsupportedException extends RuntimeException {
    public GrantTypeIsUnsupportedException(String message) {
        super(message);
    }

    public GrantTypeIsUnsupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public GrantTypeIsUnsupportedException(Throwable cause) {
        super(cause);
    }
}
