package com.iliauni.usersyncglobalservice.unit.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.usersyncglobalservice.model.ApiAccessKcClient;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.token.ApiAccessKcTokenRequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApiAccessKcTokenRequestBuilderTest {

    private ApiAccessKcTokenRequestBuilder apiAccessKcTokenRequestBuilder;
    private ObjectMapper objectMapperMock;

    @BeforeEach
    void setUp() {
        objectMapperMock = mock(ObjectMapper.class);
        apiAccessKcTokenRequestBuilder = new ApiAccessKcTokenRequestBuilder(objectMapperMock);
    }

    @Test
    void buildPasswordRequestBody_shouldReturnValidRequestBody() throws Exception {
        // given
        ApiAccessKcClient client = new ApiAccessKcClient("client-id", "client-secret", "username", "password");
        String expectedRequestBody = "{\"client_id\":\"client-id\",\"client_secret\":\"client-secret\",\"username\":\"username\",\"password\":\"password\",\"grant_type\":\"password\",\"scope\":\"openid\"}";

        // when
        when(objectMapperMock.writeValueAsString(any(Map.class))).thenReturn(expectedRequestBody);
        String actualRequestBody = apiAccessKcTokenRequestBuilder.buildPasswordRequestBody(client);

        // then
        assertEquals(expectedRequestBody, actualRequestBody);
    }

    @Test
    void buildRefreshTokenRequestBody_shouldReturnValidRequestBody() throws Exception {
        // given
        ApiAccessKcClient client = new ApiAccessKcClient("client-id", "client-secret", "username", "password");
        RefreshToken refreshToken = new RefreshToken("refresh-token-value", 3600);
        String expectedRequestBody = "{\"client_id\":\"client-id\",\"client_secret\":\"client-secret\",\"refresh_token\":{\"token\":\"refresh-token-value\",\"expiresIn\":3600},\"grant_type\":\"refresh_token\",\"scope\":\"openid\"}";

        // when
        when(objectMapperMock.writeValueAsString(any(RefreshToken.class))).thenReturn(expectedRequestBody);
        String actualRequestBody = apiAccessKcTokenRequestBuilder.buildRefreshTokenRequestBody(client, refreshToken);

        // then
        assertEquals(expectedRequestBody, actualRequestBody);
    }

    @Test
    void buildHttpRequestEntity_shouldReturnHttpEntityWithHeaders() {
        // given
        String requestBody = "dummy-request-body";

        // when
        HttpEntity<String> httpEntity = apiAccessKcTokenRequestBuilder.buildHttpRequestEntity(requestBody);

        // then
        assertEquals(requestBody, httpEntity.getBody());
        assertEquals("application/x-www-form-urlencoded", httpEntity.getHeaders().getContentType().toString());
    }

    @Test
    void getRestTemplate_shouldReturnRestTemplateInstance() {
        // when
        RestTemplate restTemplate = apiAccessKcTokenRequestBuilder.getRestTemplate();

        // then
        assertEquals(RestTemplate.class, restTemplate.getClass());
    }
}
