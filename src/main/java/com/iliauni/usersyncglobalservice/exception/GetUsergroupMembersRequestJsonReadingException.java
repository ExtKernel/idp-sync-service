package com.iliauni.usersyncglobalservice.exception;

public class GetUsergroupMembersRequestJsonReadingException extends RuntimeException {
    public GetUsergroupMembersRequestJsonReadingException(String message) {
        super(message);
    }

    public GetUsergroupMembersRequestJsonReadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public GetUsergroupMembersRequestJsonReadingException(Throwable cause) {
        super(cause);
    }
}
