package com.iliauni.idpsyncservice.unit.token;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.idpsyncservice.model.AccessToken;
import com.iliauni.idpsyncservice.model.RefreshToken;
import com.iliauni.idpsyncservice.token.ApiAccessKcTokenJsonObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ApiAccessKcTokenJsonObjectMapperTest {

    private ApiAccessKcTokenJsonObjectMapper apiAccessKcTokenJsonObjectMapper;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        apiAccessKcTokenJsonObjectMapper = new ApiAccessKcTokenJsonObjectMapper();
        objectMapper = new ObjectMapper();
    }

    @Test
    void givenValidRefreshTokenJsonNode_whenMapRefreshTokenJsonNodeToRefreshToken_shouldReturnRefreshToken() throws Exception {
        // given
        String refreshTokenJson = "{\"refresh_token\": \"api-refresh-token\", \"refresh_expires_in\": 3000}";
        JsonNode jsonNode = objectMapper.readTree(refreshTokenJson);

        // when
        RefreshToken refreshToken = apiAccessKcTokenJsonObjectMapper.mapRefreshTokenJsonNodeToRefreshToken(jsonNode);

        // should
        assertEquals("api-refresh-token", refreshToken.getToken());
        assertEquals(3000, refreshToken.getExpiresIn());
    }

    @Test
    void givenValidAccessTokenJsonNode_whenMapAccessTokenJsonNodeToRefreshToken_shouldReturnAccessToken() throws Exception {
        // given
        String accessTokenJson = "{\"access_token\": \"api-access-token\", \"expires_in\": 6000}";
        JsonNode jsonNode = objectMapper.readTree(accessTokenJson);

        // when
        AccessToken accessToken = apiAccessKcTokenJsonObjectMapper.mapAccessTokenJsonNodeToRefreshToken(jsonNode);

        // should
        assertEquals("api-access-token", accessToken.getToken());
        assertEquals(6000, accessToken.getExpiresIn());
    }
}
