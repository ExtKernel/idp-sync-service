package com.iliauni.usersyncglobalservice.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.usersyncglobalservice.model.Oauth2Client;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public abstract class GenericKcTokenRequestBuilder<T extends Oauth2Client> implements TokenRequestBuilder<T> {
    private final ObjectMapper objectMapper;

    public GenericKcTokenRequestBuilder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public HttpEntity<String> buildHttpRequestEntity(String requestBody) {
        return new HttpEntity<>(
                requestBody,
                buildHeaders()
        );
    }

    @Override
    public String buildPasswordRequestBody(T client) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("client_id", client.getId());
        requestBody.put("client_secret", client.getClientSecret());
        requestBody.put("username", client.getPrincipalUsername());
        requestBody.put("password", client.getPrincipalPassword());
        requestBody.put("grant_type", "password");
        requestBody.put("scope", "openid");

        try {
            return objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String buildRefreshTokenRequestBody(
            T client,
            RefreshToken refreshToken
    ) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("client_id", client.getId());
        requestBody.put("client_secret", client.getClientSecret());
        requestBody.put("refresh_token", refreshToken);
        requestBody.put("grant_type", "refresh_token");
        requestBody.put("scope", "openid");

        try {
            return objectMapper.writeValueAsString(refreshToken);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
