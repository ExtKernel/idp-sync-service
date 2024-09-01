package com.iliauni.idpsyncservice.unit.token;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.idpsyncservice.model.AccessToken;
import com.iliauni.idpsyncservice.model.RefreshToken;
import com.iliauni.idpsyncservice.token.SyncKcTokenJsonObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SyncKcTokenJsonObjectMapperTest {

    private SyncKcTokenJsonObjectMapper syncKcTokenJsonObjectMapper;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        syncKcTokenJsonObjectMapper = new SyncKcTokenJsonObjectMapper();
        objectMapper = new ObjectMapper();
    }

    @Test
    void givenValidRefreshTokenJsonNode_whenMapRefreshTokenJsonNodeToRefreshToken_shouldReturnRefreshToken() throws Exception {
        // given
        String refreshTokenJson = "{\"refresh_token\": \"refresh-token-value\", \"refresh_expires_in\": 3600}";
        JsonNode jsonNode = objectMapper.readTree(refreshTokenJson);

        // when
        RefreshToken refreshToken = syncKcTokenJsonObjectMapper.mapRefreshTokenJsonNodeToRefreshToken(jsonNode);

        // should
        assertEquals("refresh-token-value", refreshToken.getToken());
        assertEquals(3600, refreshToken.getExpiresIn());
    }

    @Test
    void givenValidAccessTokenJsonNode_whenMapAccessTokenJsonNodeToRefreshToken_shouldReturnAccessToken() throws Exception {
        // given
        String accessTokenJson = "{\"access_token\": \"access-token-value\", \"expires_in\": 7200}";
        JsonNode jsonNode = objectMapper.readTree(accessTokenJson);

        // when
        AccessToken accessToken = syncKcTokenJsonObjectMapper.mapAccessTokenJsonNodeToRefreshToken(jsonNode);

        // should
        assertEquals("access-token-value", accessToken.getToken());
        assertEquals(7200, accessToken.getExpiresIn());
    }
}
