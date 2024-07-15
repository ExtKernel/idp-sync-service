package com.iliauni.idpsyncservice.exception;

public class ClientNameDoesntFollowConventionException extends Exception {
    public ClientNameDoesntFollowConventionException(String message) {
        super(message);
    }

    public ClientNameDoesntFollowConventionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientNameDoesntFollowConventionException(Throwable cause) {
        super(cause);
    }
}
