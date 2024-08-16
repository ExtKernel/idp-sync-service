package com.iliauni.idpsyncservice.exception;

import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler {

    private ErrorResponse createErrorResponse(HttpStatus status, Exception exception, WebRequest request) {
        log.error(exception.getClass().getSimpleName() + " occurred: " + exception.getMessage());

        return new ErrorResponse(
                status.value(),
                new Date(),
                exception.getMessage(),
                request.getDescription(false)
        );
    }

    // Client exceptions
    @ExceptionHandler({
            ClientHasNoFqdnOrIpAndPortException.class,
            ClientIsNullException.class,
            InvalidClientCredentialsExceptions.class,
            InvalidClientPrincipalCredentialsException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleClientInternalErrors(
            Exception exception,
            WebRequest request
    ) {
        return createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception,
                request
        );
    }

    @ExceptionHandler({
            ClientNotFoundException.class,
            NoRecordOfClientPrincipalCredentialsException.class,
            NoRecordOfClientsException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleClientNotFoundErrors(
            Exception exception,
            WebRequest request
    ) {
        return createErrorResponse(
                HttpStatus.NOT_FOUND,
                exception,
                request
        );
    }

    // KcClient exceptions
    @ExceptionHandler({
            KcClientHasNoKcFqdnOrIpAndPortException.class,
            KcClientHasNoKcRealmException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleKcClientErrors(
            Exception exception,
            WebRequest request
    ) {
        return createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception,
                request
        );
    }

    // Token exceptions
    @ExceptionHandler({
            KcTokenIsNullException.class,
            NullTokenException.class,
            RefreshTokenIsNullException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleTokenInternalErrors(
            Exception exception,
            WebRequest request
    ) {
        return createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception,
                request
        );
    }

    @ExceptionHandler({
            NoRecordOfRefreshTokenException.class,
            NoRecordOfRefreshTokenForTheClientException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleTokenNotFoundErrors(
            Exception exception,
            WebRequest request
    ) {
        return createErrorResponse(
                HttpStatus.NOT_FOUND,
                exception,
                request
        );
    }

    // User exceptions
    @ExceptionHandler({
            KcUserWithUsernameNotFoundException.class,
            UserAlreadyExistsOnTheClientException.class,
            UserDoesNotExistOnTheClientException.class,
            UserIsNullException.class,
            UserJsonReadingException.class,
            UsersJsonReadingException.class,
            UserToJsonMappingException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUserErrors(
            Exception exception,
            WebRequest request
    ) {
        return createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception,
                request
        );
    }

    @ExceptionHandler(NoRecordOfUsersException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoRecordOfUsersException(
            NoRecordOfUsersException exception,
            WebRequest request
    ) {
        return createErrorResponse(
                HttpStatus.NOT_FOUND,
                exception,
                request
        );
    }

    // Usergroup exceptions
    @ExceptionHandler({
            KcUsergroupWithNameNotFoundException.class,
            UsergroupAlreadyExistsOnTheClientException.class,
            UsergroupDoesNotExistOnTheClientException.class,
            UsergroupIsNullException.class,
            UsergroupJsonReadingException.class,
            UsergroupMemberAlreadyExistsOnTheClientException.class,
            UsergroupMemberDoesNotExistOnTheClientException.class,
            UsergroupMembersJsonReadingException.class,
            UsergroupToJsonMappingException.class,
            UsergroupsDoNotExistOnTheClientException.class,
            UsergroupsJsonReadingException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUsergroupErrors(
            Exception exception,
            WebRequest request
    ) {
        return createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception,
                request
        );
    }

    @ExceptionHandler(NoRecordOfUsergroupsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoRecordOfUsergroupsException(
            NoRecordOfUsergroupsException exception,
            WebRequest request
    ) {
        return createErrorResponse(
                HttpStatus.NOT_FOUND,
                exception,
                request
        );
    }

    // Other exceptions
    @ExceptionHandler({
            CacheDoesNotExistException.class,
            CookieJarIsNullException.class,
            CredentialsRepresentationBuildingException.class,
            GrantTypeIsUnsupportedException.class,
            ModelIsNullException.class,
            ModelNotFoundException.class,
            NoRecordOfCookieJarException.class,
            WritingRequestBodyToStringException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherErrors(
            Exception exception,
            WebRequest request
    ) {
        return createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception,
                request
        );
    }
}
