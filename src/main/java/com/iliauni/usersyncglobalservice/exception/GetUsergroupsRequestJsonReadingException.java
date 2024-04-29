package com.iliauni.usersyncglobalservice.exception;

public class GetUsergroupsRequestJsonReadingException extends RuntimeException {
    public GetUsergroupsRequestJsonReadingException(String message) {
        super(message);
    }

    public GetUsergroupsRequestJsonReadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public GetUsergroupsRequestJsonReadingException(Throwable cause) {
        super(cause);
    }
}
