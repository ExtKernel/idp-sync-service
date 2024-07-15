package com.iliauni.idpsyncservice.exception;

public class KcUsergroupWithNameNotFoundException extends RuntimeException {
    public KcUsergroupWithNameNotFoundException(String message) {
        super(message);
    }

    public KcUsergroupWithNameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public KcUsergroupWithNameNotFoundException(Throwable cause) {
        super(cause);
    }
}
