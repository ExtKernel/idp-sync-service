package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.KcClient;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Component
public class KcTokenRequestSender<T extends KcClient> implements TokenRequestSender<T> {
    private KcTokenRequestBuilder<T> requestBuilder;
    private TokenObjectMapper objectMapper;
    private RestTemplate restTemplate;

    @Autowired
    public KcTokenRequestSender(
            RestTemplateBuilder restTemplateBuilder,
            KcTokenRequestBuilder<T> requestBuilder,
            TokenObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
        this.requestBuilder = requestBuilder;
        this.objectMapper = objectMapper;
    }

    @Override
    public AccessToken getAccessTokenByRefreshToken(
            T client,
            String tokenEndpointUrl) {
        return objectMapper.mapAccessTokenJsonMapToRefreshToken((Map<String, Object>)
                restTemplate.exchange(
                        tokenEndpointUrl,
                        HttpMethod.POST,
                        requestBuilder.buildHttpRequestEntityWithRefreshTokenGrantType(client),
                        Map.class).getBody());
    }

    @Override
    public AccessToken getAccessTokenByCredentials(
            T client,
            String tokenEndpointUrl) {
        return objectMapper.mapAccessTokenJsonMapToRefreshToken((Map<String, Object>)
                restTemplate.exchange(
                        tokenEndpointUrl,
                        HttpMethod.POST,
                        requestBuilder.buildHttpRequestEntityWithPasswordGrantType(client),
                        Map.class).getBody()
        );
    }

    @Override
    public RefreshToken getRefreshToken(
            T client,
            String tokenEndpointUrl) {
        return objectMapper.mapRefreshTokenJsonMapToRefreshToken((Map<String, Object>)
                restTemplate.exchange(
                        tokenEndpointUrl,
                        HttpMethod.POST,
                        requestBuilder.buildHttpRequestEntityWithPasswordGrantType(client),
                        Map.class).getBody()
        );
    }
}
