package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.model.KcClient;
import com.iliauni.usersyncglobalservice.service.KcClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class KcTokenRequestBuilder<T extends KcClient> implements TokenRequestBuilder<T> {
    @Lazy
    private KcClientService<T> clientService;

    @Autowired
    public KcTokenRequestBuilder(KcClientService<T> clientService) {
        this.clientService = clientService;
    }

    public HttpEntity<MultiValueMap<String, String>> buildHttpRequestEntityWithPasswordGrantType(T client) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", client.getId());
        requestBody.add("client_secret", client.getClientSecret());
        requestBody.add("username", client.getPrincipalPassword());
        requestBody.add("password", client.getPrincipalPassword());
        requestBody.add("grant_type", "password");

        return new HttpEntity<>(requestBody, setHeaders());
    }

    public HttpEntity<MultiValueMap<String, String>> buildHttpRequestEntityWithRefreshTokenGrantType(T client) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", client.getId());
        requestBody.add("client_secret", client.getClientSecret());
        requestBody.add("refresh_token", clientService.getLatestRefreshToken(client.getId()).getToken());
        requestBody.add("grant_type", "refresh_token");

        return new HttpEntity<>(requestBody, setHeaders());
    }

    private HttpHeaders setHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return headers;
    }
}
