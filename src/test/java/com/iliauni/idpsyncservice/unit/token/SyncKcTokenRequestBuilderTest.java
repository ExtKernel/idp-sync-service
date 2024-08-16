package com.iliauni.idpsyncservice.unit.token;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.iliauni.idpsyncservice.model.RefreshToken;
import com.iliauni.idpsyncservice.model.SyncKcClient;
import com.iliauni.idpsyncservice.token.SyncKcTokenRequestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class SyncKcTokenRequestBuilderTest {

    private final SyncKcTokenRequestBuilder syncKcTokenRequestBuilder = new SyncKcTokenRequestBuilder();

    @Test
    void buildPasswordRequestBody_shouldReturnValidRequestBody() throws Exception {
        // given
        SyncKcClient client = new SyncKcClient(
                "client-id",
                "client-secret",
                "test-fqdn"
        );
        client.setPrincipalUsername("username");
        client.setPrincipalPassword("password");

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("client_id", "client-id");
        multiValueMap.add("client_secret", "client-secret");
        multiValueMap.add("username", "username");
        multiValueMap.add("password", "password");
        multiValueMap.add("grant_type", "password");
        multiValueMap.add("scope", "openid");
        // when
        MultiValueMap<String, String> actualRequestBody = syncKcTokenRequestBuilder.buildPasswordRequestBody(client);

        // then
        assertEquals(multiValueMap, actualRequestBody);
    }

    @Test
    void buildRefreshTokenRequestBody_shouldReturnValidRequestBody() throws Exception {
        // given
        SyncKcClient client = new SyncKcClient(
                "client-id",
                "client-secret",
                "test-fqdn"
        );
        RefreshToken refreshToken = new RefreshToken("refresh-token-value", 3600);

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("client_id", "client-id");
        multiValueMap.add("client_secret", "client-secret");
        multiValueMap.add("refresh_token", "refresh-token-value");
        multiValueMap.add("grant_type", "refresh_token");
        multiValueMap.add("scope", "openid");

        // when
        MultiValueMap<String, String> actualRequestBody = syncKcTokenRequestBuilder.buildRefreshTokenRequestBody(client, refreshToken);

        // then
        assertEquals(multiValueMap, actualRequestBody);
    }

    @Test
    void buildHttpRequestEntity_shouldReturnHttpEntityWithHeaders() {
        // given
        MultiValueMap requestBody = new LinkedMultiValueMap();

        // when
        HttpEntity<String> httpEntity = syncKcTokenRequestBuilder.buildHttpRequestEntity(requestBody);

        // then
        assertEquals(requestBody, httpEntity.getBody());
        assertEquals("application/x-www-form-urlencoded", httpEntity.getHeaders().getContentType().toString());
    }

    @Test
    void getRestTemplate_shouldReturnRestTemplateInstance() {
        // when
        RestTemplate restTemplate = syncKcTokenRequestBuilder.getRestTemplate();

        // then
        assertEquals(RestTemplate.class, restTemplate.getClass());
    }
}
