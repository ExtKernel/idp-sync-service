package com.iliauni.idpsyncservice.exception;

public class InvalidClientCredentialsExceptions extends RuntimeException {
    public InvalidClientCredentialsExceptions(String message) {
        super(message);
    }

    public InvalidClientCredentialsExceptions(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidClientCredentialsExceptions(Throwable cause) {
        super(cause);
    }
}
