package com.iliauni.usersyncglobalservice.unit.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.model.WinClient;
import com.iliauni.usersyncglobalservice.token.WinTokenRequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WinTokenRequestBuilderTest {

    private WinTokenRequestBuilder winTokenRequestBuilder;
    private ObjectMapper objectMapperMock;

    @BeforeEach
    void setUp() {
        objectMapperMock = mock(ObjectMapper.class);
        winTokenRequestBuilder = new WinTokenRequestBuilder(objectMapperMock);
    }

    @Test
    void buildPasswordRequestBody_shouldReturnValidRequestBody() throws Exception {
        // given
        WinClient client = new WinClient("win-client-id", "win-client-secret", "win-username", "win-password");
        String expectedRequestBody = "{\"client_id\":\"win-client-id\",\"client_secret\":\"win-client-secret\",\"username\":\"win-username\",\"password\":\"win-password\",\"grant_type\":\"password\",\"scope\":\"openid\"}";

        // when
        when(objectMapperMock.writeValueAsString(any(Map.class))).thenReturn(expectedRequestBody);
        String actualRequestBody = winTokenRequestBuilder.buildPasswordRequestBody(client);

        // then
        assertEquals(expectedRequestBody, actualRequestBody);
    }

    @Test
    void buildRefreshTokenRequestBody_shouldReturnValidRequestBody() throws Exception {
        // given
        WinClient client = new WinClient("win-client-id", "win-client-secret", "win-username", "win-password");
        RefreshToken refreshToken = new RefreshToken("win-refresh-token-value", 3600);
        String expectedRequestBody = "{\"client_id\":\"win-client-id\",\"client_secret\":\"win-client-secret\",\"refresh_token\":{\"token\":\"win-refresh-token-value\",\"expiresIn\":3600},\"grant_type\":\"refresh_token\",\"scope\":\"openid\"}";

        // when
        when(objectMapperMock.writeValueAsString(any(RefreshToken.class))).thenReturn(expectedRequestBody);
        String actualRequestBody = winTokenRequestBuilder.buildRefreshTokenRequestBody(client, refreshToken);

        // then
        assertEquals(expectedRequestBody, actualRequestBody);
    }

    @Test
    void buildHttpRequestEntity_shouldReturnHttpEntityWithHeaders() {
        // given
        String requestBody = "dummy-request-body";

        // when
        HttpEntity<String> httpEntity = winTokenRequestBuilder.buildHttpRequestEntity(requestBody);

        // then
        assertEquals(requestBody, httpEntity.getBody());
        assertEquals("application/x-www-form-urlencoded", httpEntity.getHeaders().getContentType().toString());
    }

    @Test
    void getRestTemplate_shouldReturnRestTemplateInstance() {
        // when
        RestTemplate restTemplate = winTokenRequestBuilder.getRestTemplate();

        // then
        assertEquals(RestTemplate.class, restTemplate.getClass());
    }
}
