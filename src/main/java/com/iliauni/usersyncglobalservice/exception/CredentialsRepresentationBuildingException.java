package com.iliauni.usersyncglobalservice.exception;

public class CredentialsRepresentationBuildingException extends RuntimeException {
    public CredentialsRepresentationBuildingException(String message) {
        super(message);
    }

    public CredentialsRepresentationBuildingException(String message, Throwable cause) {
        super(message, cause);
    }

    public CredentialsRepresentationBuildingException(Throwable cause) {
        super(cause);
    }
}
