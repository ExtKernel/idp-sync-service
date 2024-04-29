package com.iliauni.usersyncglobalservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler {
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidClientPrincipalCredentialsException.class)
    public ErrorResponse handleInvalidClientPrincipalCredentialsException(
            InvalidClientPrincipalCredentialsException exception,
            WebRequest request
    ) {
        log.error("InvalidClientPrincipalCredentialsException occurred: " + exception.getMessage());

        return new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                exception.getMessage(),
                request.getDescription(false)
        );
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidClientCredentialsExceptions.class)
    public ErrorResponse handleInvalidClientCredentialsException(
            InvalidClientCredentialsExceptions exception,
            WebRequest request
    ) {
        log.error("InvalidClientCredentialsExceptions occurred: " + exception.getMessage());

        return new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                exception.getMessage(),
                request.getDescription(false)
        );
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(GrantTypeIsUnsupportedException.class)
    public ErrorResponse handleGrantTypeIsUnsupportedException(
            GrantTypeIsUnsupportedException exception,
            WebRequest request
    ) {
        log.error("GrantTypeIsUnsupportedException occurred: " + exception.getMessage()
                + ". Consider changing to \"password\" or \"refresh_token\"");

        return new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                exception.getMessage(),
                request.getDescription(false)
        );
    }
}
