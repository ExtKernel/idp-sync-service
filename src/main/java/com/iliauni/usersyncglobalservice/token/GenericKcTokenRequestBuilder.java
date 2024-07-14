package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.model.Oauth2Client;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public abstract class GenericKcTokenRequestBuilder<T extends Oauth2Client> implements TokenRequestBuilder<T> {

    @Override
    public HttpEntity<Map<String, String>> buildHttpRequestEntity(Map<String, String> requestBody) {
        return new HttpEntity<>(
                requestBody,
                buildHeaders()
        );
    }

    @Override
    public MultiValueMap<String, String> buildPasswordRequestBody(T client) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", client.getId());
        requestBody.add("client_secret", client.getClientSecret());
        requestBody.add("username", client.getPrincipalUsername());
        requestBody.add("password", client.getPrincipalPassword());
        requestBody.add("grant_type", "password");
        requestBody.add("scope", "openid");

        return requestBody;
    }

    @Override
    public MultiValueMap<String, String> buildRefreshTokenRequestBody(
            T client,
            RefreshToken refreshToken
    ) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", client.getId());
        requestBody.add("client_secret", client.getClientSecret());
        requestBody.add("refresh_token", refreshToken.getToken());
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("scope", "openid");

        return requestBody;
    }

    @Override
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return headers;
    }
}
