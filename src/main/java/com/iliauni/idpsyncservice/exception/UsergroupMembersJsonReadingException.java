package com.iliauni.idpsyncservice.exception;

public class UsergroupMembersJsonReadingException extends RuntimeException {
    public UsergroupMembersJsonReadingException(String message) {
        super(message);
    }

    public UsergroupMembersJsonReadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsergroupMembersJsonReadingException(Throwable cause) {
        super(cause);
    }
}
