package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.model.Oauth2Client;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class GenericKcTokenRequestBuilder<T extends Oauth2Client> implements TokenRequestBuilder<T> {
    public HttpEntity<MultiValueMap<String, String>> buildHttpRequestEntityWithPasswordGrantType(T client) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", client.getId());
        requestBody.add("client_secret", client.getClientSecret());
        requestBody.add("username", client.getPrincipalUsername());
        requestBody.add("password", client.getPrincipalPassword());
        requestBody.add("grant_type", "password");
        requestBody.add("scope", "openid");

        return new HttpEntity<>(requestBody, setHeaders());
    }

    public HttpEntity<MultiValueMap<String, String>> buildHttpRequestEntityWithRefreshTokenGrantType(
            T client,
            String refreshToken
    ) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", client.getId());
        requestBody.add("client_secret", client.getClientSecret());
        requestBody.add("refresh_token", refreshToken);
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("scope", "openid");

        return new HttpEntity<>(requestBody, setHeaders());
    }

    HttpHeaders setHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return headers;
    }
}
