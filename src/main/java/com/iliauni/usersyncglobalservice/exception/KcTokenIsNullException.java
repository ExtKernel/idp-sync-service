package com.iliauni.usersyncglobalservice.exception;

public class KcTokenIsNullException extends Exception {
    public KcTokenIsNullException(String message) {
        super(message);
    }

    public KcTokenIsNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public KcTokenIsNullException(Throwable cause) {
        super(cause);
    }
}
