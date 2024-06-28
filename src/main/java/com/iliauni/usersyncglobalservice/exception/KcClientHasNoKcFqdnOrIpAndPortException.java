package com.iliauni.usersyncglobalservice.exception;

public class KcClientHasNoKcFqdnOrIpAndPortException extends RuntimeException {
    public KcClientHasNoKcFqdnOrIpAndPortException(String message) {
        super(message);
    }

    public KcClientHasNoKcFqdnOrIpAndPortException(String message, Throwable cause) {
        super(message, cause);
    }

    public KcClientHasNoKcFqdnOrIpAndPortException(Throwable cause) {
        super(cause);
    }
}
