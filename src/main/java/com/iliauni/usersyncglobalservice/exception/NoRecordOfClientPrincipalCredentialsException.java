package com.iliauni.usersyncglobalservice.exception;

public class NoRecordOfClientPrincipalCredentialsException extends RuntimeException {
    public NoRecordOfClientPrincipalCredentialsException(String message) {
        super(message);
    }

    public NoRecordOfClientPrincipalCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoRecordOfClientPrincipalCredentialsException(Throwable cause) {
        super(cause);
    }
}
