package com.iliauni.usersyncglobalservice.exception;

public class WritingRequestBodyToStringException extends RuntimeException {
    public WritingRequestBodyToStringException(String message) {
        super(message);
    }

    public WritingRequestBodyToStringException(String message, Throwable cause) {
        super(message, cause);
    }

    public WritingRequestBodyToStringException(Throwable cause) {
        super(cause);
    }
}
