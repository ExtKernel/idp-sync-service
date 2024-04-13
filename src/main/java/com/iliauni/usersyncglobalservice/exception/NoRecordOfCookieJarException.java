package com.iliauni.usersyncglobalservice.exception;

public class NoRecordOfCookieJarException extends RuntimeException {
    public NoRecordOfCookieJarException(String message) {
        super(message);
    }

    public NoRecordOfCookieJarException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoRecordOfCookieJarException(Throwable cause) {
        super(cause);
    }
}
