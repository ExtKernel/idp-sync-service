package com.iliauni.idpsyncservice.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.idpsyncservice.model.Oauth2Client;
import com.iliauni.idpsyncservice.model.RefreshToken;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public abstract class GenericTokenRequestSender<T extends Oauth2Client> implements TokenRequestSender<T> {
    private final ObjectMapper objectMapper;
    private final TokenRequestBuilder<T> requestBuilder;
    private final RestTemplate restTemplate;

    public GenericTokenRequestSender(
            ObjectMapper objectMapper,
            TokenRequestBuilder<T> requestBuilder
    ) {
        this.objectMapper = objectMapper;
        this.requestBuilder = requestBuilder;
        this.restTemplate = requestBuilder.getRestTemplate();
    }

    @Override
    public JsonNode getAccessToken(
            T client,
            String tokenEndpointUrl,
            RefreshToken refreshToken
    ) {
        try {
            return objectMapper.readTree(restTemplate.exchange(
                    tokenEndpointUrl,
                    HttpMethod.POST,
                    requestBuilder.buildHttpRequestEntity(
                            requestBuilder.buildRefreshTokenRequestBody(
                                    client,
                                    refreshToken
                            )
                    ),
                    String.class
            ).getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JsonNode getRefreshToken(
            T client,
            String tokenEndpointUrl
    ) {
        try {
            System.out.println(client.getId());
            System.out.println(tokenEndpointUrl);

            return objectMapper.readTree(restTemplate.exchange(
                    tokenEndpointUrl,
                    HttpMethod.POST,
                    requestBuilder.buildHttpRequestEntity(
                            requestBuilder.buildPasswordRequestBody(client)
                    ),
                    String.class
            ).getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
