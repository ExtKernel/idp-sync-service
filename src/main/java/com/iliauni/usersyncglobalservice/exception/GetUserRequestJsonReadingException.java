package com.iliauni.usersyncglobalservice.exception;

public class GetUserRequestJsonReadingException extends RuntimeException {
    public GetUserRequestJsonReadingException(String message) {
        super(message);
    }

    public GetUserRequestJsonReadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public GetUserRequestJsonReadingException(Throwable cause) {
        super(cause);
    }
}
