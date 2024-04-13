package com.iliauni.usersyncglobalservice.exception;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.NoArgsConstructor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

@NoArgsConstructor
@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().is4xxClientError()) {
            String responseBodyText = new String(
                    response.getBody().readAllBytes(), StandardCharsets.UTF_8);

            if (responseBodyText.contains("Invalid user credentials")) {
                throw new
                        InvalidClientPrincipalCredentialsException(
                                "Client principal user credentials are invalid");
            } else if (responseBodyText.contains("Invalid client or Invalid client credentials")) {
                throw new
                        InvalidClientCredentialsExceptions(
                                "Client ID or Client Secret is invalid");
            } else if (responseBodyText.contains("Unsupported grant_type")) {
                throw new
                        GrantTypeIsUnsupportedException("Grant type is unsupported");
            }
        }
    }
}
