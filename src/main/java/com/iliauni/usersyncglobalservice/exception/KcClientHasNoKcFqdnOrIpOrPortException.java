package com.iliauni.usersyncglobalservice.exception;

public class KcClientHasNoKcFqdnOrIpOrPortException extends RuntimeException {
    public KcClientHasNoKcFqdnOrIpOrPortException(String message) {
        super(message);
    }

    public KcClientHasNoKcFqdnOrIpOrPortException(String message, Throwable cause) {
        super(message, cause);
    }

    public KcClientHasNoKcFqdnOrIpOrPortException(Throwable cause) {
        super(cause);
    }
}
