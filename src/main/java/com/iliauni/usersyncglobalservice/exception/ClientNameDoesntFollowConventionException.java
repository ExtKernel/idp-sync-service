package com.iliauni.usersyncglobalservice.exception;

public class ClientNameDoesntFollowConventionException extends RuntimeException {
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
