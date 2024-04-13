package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.exception.GrantTypeIsUnsupportedException;
import com.iliauni.usersyncglobalservice.model.Oauth2Client;
import com.iliauni.usersyncglobalservice.service.Oauth2ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Objects;

@Component
public class TokenRequestBuilder<T extends Oauth2Client> {
    @Lazy
    private Oauth2ClientService<T> clientService;

    @Autowired
    public TokenRequestBuilder(Oauth2ClientService<T> clientService) {
        this.clientService = clientService;
    }

    public HttpEntity<MultiValueMap<String, String>> buildHttpRequestEntity(String grantType, T client) {
        if (Objects.equals(grantType, "password")) {
            return buildHttpRequestEntityWithPasswordGrantType(
                    client.getId(),
                    client.getClientSecret(),
                    client.getPrincipalUsername(),
                    client.getPrincipalPassword()
            );
        } else if (Objects.equals(grantType, "refresh_token")) {
            return buildHttpRequestEntityWithRefreshTokenGrantType(
                    client.getId(),
                    client.getClientSecret(),
                    clientService.getLatestRefreshToken(client.getId()).getToken()
            );
        } else {
            throw new
                    GrantTypeIsUnsupportedException("Grant type is unsupported");
        }
    }

    private HttpEntity<MultiValueMap<String, String>> buildHttpRequestEntityWithPasswordGrantType(
            String clientId,
            String clientSecret,
            String principalUsername,
            String principalPassword) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("grant_type", "password");
        requestBody.add("username", principalUsername);
        requestBody.add("password", principalUsername);

        return new HttpEntity<>(requestBody, setHeaders());
    }

    private HttpEntity<MultiValueMap<String, String>> buildHttpRequestEntityWithRefreshTokenGrantType(
            String clientId,
            String clientSecret,
            String refreshToken) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("refresh_token", refreshToken);

        return new HttpEntity<>(requestBody, setHeaders());
    }

    private HttpHeaders setHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return headers;
    }
}
