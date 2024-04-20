package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.model.ApiAccessKcClient;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class ApiAccessKcTokenRequestBuilder extends GenericKcTokenRequestBuilder<ApiAccessKcClient> {
    @Override
    public HttpEntity<MultiValueMap<String, String>> buildHttpRequestEntityWithPasswordGrantType(ApiAccessKcClient client) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", client.getId());
        requestBody.add("username", client.getPrincipalUsername());
        requestBody.add("password", client.getPrincipalPassword());
        requestBody.add("grant_type", "password");
        requestBody.add("scope", "openid");

        return new HttpEntity<>(requestBody, setHeaders());
    }
}
