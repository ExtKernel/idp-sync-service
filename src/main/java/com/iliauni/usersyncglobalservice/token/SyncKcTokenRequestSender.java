package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.model.SyncKcClient;
import com.iliauni.usersyncglobalservice.service.Oauth2ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class SyncKcTokenRequestSender implements TokenRequestSender<SyncKcClient> {
    private final TokenRequestBuilder<SyncKcClient> requestBuilder;
    private final Oauth2ClientService<SyncKcClient> clientService;
    private final TokenObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Autowired
    public SyncKcTokenRequestSender(
            TokenRequestBuilder<SyncKcClient> requestBuilder,
            Oauth2ClientService<SyncKcClient> clientService,
            TokenObjectMapper objectMapper,
            RestTemplateBuilder restTemplateBuilder
    ) {
        this.requestBuilder = requestBuilder;
        this.clientService = clientService;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
    }

    @Override
    public AccessToken getAccessTokenByCredentials(
            SyncKcClient client,
            String tokenEndpointUrl
    ) {
        return objectMapper.mapAccessTokenJsonMapToRefreshToken((Map<String, Object>)
                restTemplate.exchange(
                        tokenEndpointUrl,
                        HttpMethod.POST,
                        requestBuilder.buildHttpRequestEntityWithPasswordGrantType(client),
                        Map.class).getBody()
        );
    }

    @Override
    public AccessToken getAccessTokenByRefreshToken(
            SyncKcClient client,
            String tokenEndpointUrl
    ) {
        return objectMapper.mapAccessTokenJsonMapToRefreshToken((Map<String, Object>)
                restTemplate.exchange(
                        tokenEndpointUrl,
                        HttpMethod.POST,
                        requestBuilder.buildHttpRequestEntityWithRefreshTokenGrantType(
                                client,
                                clientService.getRefreshToken(
                                        client.getId(),
                                        tokenEndpointUrl
                                ).getToken()
                        ),
                        Map.class).getBody()
        );
    }

    @Override
    public RefreshToken getRefreshToken(
            SyncKcClient client,
            String tokenEndpointUrl
    ) {
        return objectMapper.mapRefreshTokenJsonMapToRefreshToken((Map<String, Object>)
                restTemplate.exchange(
                        tokenEndpointUrl,
                        HttpMethod.POST,
                        requestBuilder.buildHttpRequestEntityWithPasswordGrantType(client),
                        Map.class
                ).getBody()
        );
    }
}
