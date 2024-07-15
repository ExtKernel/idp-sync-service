package com.iliauni.idpsyncservice.exception;

public class NoRecordOfUsersException extends RuntimeException {
    public NoRecordOfUsersException(String message) {
        super(message);
    }

    public NoRecordOfUsersException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoRecordOfUsersException(Throwable cause) {
        super(cause);
    }
}
