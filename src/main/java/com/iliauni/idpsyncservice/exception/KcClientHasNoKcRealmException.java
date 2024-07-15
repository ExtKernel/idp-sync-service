package com.iliauni.idpsyncservice.exception;

public class KcClientHasNoKcRealmException extends RuntimeException {
    public KcClientHasNoKcRealmException(String message) {
        super(message);
    }

    public KcClientHasNoKcRealmException(String message, Throwable cause) {
        super(message, cause);
    }

    public KcClientHasNoKcRealmException(Throwable cause) {
        super(cause);
    }
}
