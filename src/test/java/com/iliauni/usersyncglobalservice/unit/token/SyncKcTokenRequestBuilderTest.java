package com.iliauni.usersyncglobalservice.unit.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.model.SyncKcClient;
import com.iliauni.usersyncglobalservice.token.SyncKcTokenRequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SyncKcTokenRequestBuilderTest {

    private SyncKcTokenRequestBuilder syncKcTokenRequestBuilder;
    private ObjectMapper objectMapperMock;

    @BeforeEach
    void setUp() {
        objectMapperMock = mock(ObjectMapper.class);
        syncKcTokenRequestBuilder = new SyncKcTokenRequestBuilder(objectMapperMock);
    }

    @Test
    void buildPasswordRequestBody_shouldReturnValidRequestBody() throws Exception {
        // given
        SyncKcClient client = new SyncKcClient("sync-client-id", "sync-client-secret", "sync-username", "sync-password");
        String expectedRequestBody = "{\"client_id\":\"sync-client-id\",\"client_secret\":\"sync-client-secret\",\"username\":\"sync-username\",\"password\":\"sync-password\",\"grant_type\":\"password\",\"scope\":\"openid\"}";

        // when
        when(objectMapperMock.writeValueAsString(any(Map.class))).thenReturn(expectedRequestBody);
        String actualRequestBody = syncKcTokenRequestBuilder.buildPasswordRequestBody(client);

        // then
        assertEquals(expectedRequestBody, actualRequestBody);
    }

    @Test
    void buildRefreshTokenRequestBody_shouldReturnValidRequestBody() throws Exception {
        // given
        SyncKcClient client = new SyncKcClient("sync-client-id", "sync-client-secret", "sync-username", "sync-password");
        RefreshToken refreshToken = new RefreshToken("sync-refresh-token-value", 3600);
        String expectedRequestBody = "{\"client_id\":\"sync-client-id\",\"client_secret\":\"sync-client-secret\",\"refresh_token\":{\"token\":\"sync-refresh-token-value\",\"expiresIn\":3600},\"grant_type\":\"refresh_token\",\"scope\":\"openid\"}";

        // when
        when(objectMapperMock.writeValueAsString(any(RefreshToken.class))).thenReturn(expectedRequestBody);
        String actualRequestBody = syncKcTokenRequestBuilder.buildRefreshTokenRequestBody(client, refreshToken);

        // then
        assertEquals(expectedRequestBody, actualRequestBody);
    }

    @Test
    void buildHttpRequestEntity_shouldReturnHttpEntityWithHeaders() {
        // given
        String requestBody = "dummy-request-body";

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
