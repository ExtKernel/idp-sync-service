package com.iliauni.idpsyncservice.exception;

public class NoRecordOfUsergroupsException extends RuntimeException {
    public NoRecordOfUsergroupsException(String message) {
        super(message);
    }

    public NoRecordOfUsergroupsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoRecordOfUsergroupsException(Throwable cause) {
        super(cause);
    }
}
