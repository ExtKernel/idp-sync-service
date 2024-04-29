package com.iliauni.usersyncglobalservice.exception;

public class GetUsersRequestJsonReadingException extends RuntimeException {
    public GetUsersRequestJsonReadingException(String message) {
        super(message);
    }

    public GetUsersRequestJsonReadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public GetUsersRequestJsonReadingException(Throwable cause) {
        super(cause);
    }
}
