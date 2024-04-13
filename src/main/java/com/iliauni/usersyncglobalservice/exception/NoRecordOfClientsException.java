package com.iliauni.usersyncglobalservice.exception;

public class NoRecordOfClientsException extends RuntimeException {
    public NoRecordOfClientsException(String message) {
        super(message);
    }

    public NoRecordOfClientsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoRecordOfClientsException(Throwable cause) {
        super(cause);
    }
}
