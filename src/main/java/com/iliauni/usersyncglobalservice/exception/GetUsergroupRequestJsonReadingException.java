package com.iliauni.usersyncglobalservice.exception;

public class GetUsergroupRequestJsonReadingException extends RuntimeException {
    public GetUsergroupRequestJsonReadingException(String message) {
        super(message);
    }

    public GetUsergroupRequestJsonReadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public GetUsergroupRequestJsonReadingException(Throwable cause) {
        super(cause);
    }
}
