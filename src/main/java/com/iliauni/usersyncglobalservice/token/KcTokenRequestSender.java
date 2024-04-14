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
    private TokenExtractor extractor;
    private RestTemplate restTemplate;

    @Autowired
    public KcTokenRequestSender(
            RestTemplateBuilder restTemplateBuilder,
            KcTokenRequestBuilder<T> requestBuilder,
            TokenExtractor extractor) {
        this.restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
        this.requestBuilder = requestBuilder;
        this.extractor = extractor;
    }

    @Override
    public AccessToken getAccessTokenByRefreshToken(
            T client,
            String tokenEndpointUrl) {
        return extractor.extractAccessTokenFromJsonMap(
                (Map<?, ?>) restTemplate.exchange(
                        tokenEndpointUrl,
                        HttpMethod.POST,
                        requestBuilder.buildHttpRequestEntity(
                                "refresh_token",
                                client),
                        Map.class).getBody());
    }

    @Override
    public AccessToken getAccessTokenByCredentials(
            T client,
            String tokenEndpointUrl) {
        return extractor.extractAccessTokenFromJsonMap(
                (Map<?, ?>) restTemplate.exchange(
                        tokenEndpointUrl,
                        HttpMethod.POST,
                        requestBuilder.buildHttpRequestEntity(
                                "password",
                                client),
                        Map.class).getBody()
        );
    }

    @Override
    public RefreshToken getRefreshToken(
            T client,
            String tokenEndpointUrl) {
        return extractor.extractRefreshTokenFromJsonMap(
                (Map<?, ?>) restTemplate.exchange(
                        tokenEndpointUrl,
                        HttpMethod.POST,
                        requestBuilder.buildHttpRequestEntity(
                                "password",
                                client),
                        Map.class).getBody()
        );
    }
}
