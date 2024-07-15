package com.iliauni.idpsyncservice.exception;

public class CookieJarIsNullException extends RuntimeException {
    public CookieJarIsNullException(String message) {
        super(message);
    }

    public CookieJarIsNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public CookieJarIsNullException(Throwable cause) {
        super(cause);
    }
}
