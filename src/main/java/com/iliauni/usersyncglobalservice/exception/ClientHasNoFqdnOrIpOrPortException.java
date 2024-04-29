package com.iliauni.usersyncglobalservice.exception;

public class ClientHasNoFqdnOrIpOrPortException extends RuntimeException {
    public ClientHasNoFqdnOrIpOrPortException(String message) {
        super(message);
    }

    public ClientHasNoFqdnOrIpOrPortException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientHasNoFqdnOrIpOrPortException(Throwable cause) {
        super(cause);
    }
}
