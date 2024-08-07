package com.iliauni.idpsyncservice.exception;

public class CacheDoesNotExistException extends RuntimeException {
    public CacheDoesNotExistException(String message) {
        super(message);
    }

    public CacheDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheDoesNotExistException(Throwable cause) {
        super(cause);
    }
}
