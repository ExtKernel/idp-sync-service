package com.iliauni.idpsyncservice.exception;

public class InvalidClientPrincipalCredentialsException extends RuntimeException {
    public InvalidClientPrincipalCredentialsException(String message) {
        super(message);
    }

    public InvalidClientPrincipalCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidClientPrincipalCredentialsException(Throwable cause) {
        super(cause);
    }
}
