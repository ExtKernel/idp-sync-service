package com.iliauni.idpsyncservice.exception;

public class ClientHasNoFqdnOrIpAndPortException extends RuntimeException {
    public ClientHasNoFqdnOrIpAndPortException(String message) {
        super(message);
    }

    public ClientHasNoFqdnOrIpAndPortException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientHasNoFqdnOrIpAndPortException(Throwable cause) {
        super(cause);
    }
}
