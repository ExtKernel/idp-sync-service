package com.iliauni.usersyncglobalservice.unit.token;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.token.WinTokenJsonObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WinTokenJsonObjectMapperTest {

    private WinTokenJsonObjectMapper winTokenJsonObjectMapper;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        winTokenJsonObjectMapper = new WinTokenJsonObjectMapper();
        objectMapper = new ObjectMapper();
    }

    @Test
    void givenValidRefreshTokenJsonNode_whenMapRefreshTokenJsonNodeToRefreshToken_shouldReturnRefreshToken() throws Exception {
        // given
        String refreshTokenJson = "{\"refresh_token\": \"win-refresh-token\", \"refresh_expires_in\": 3600}";
        JsonNode jsonNode = objectMapper.readTree(refreshTokenJson);

        // when
        RefreshToken refreshToken = winTokenJsonObjectMapper.mapRefreshTokenJsonNodeToRefreshToken(jsonNode);

        // should
        assertEquals("win-refresh-token", refreshToken.getToken());
        assertEquals(3600, refreshToken.getExpiresIn());
    }

    @Test
    void givenValidAccessTokenJsonNode_whenMapAccessTokenJsonNodeToRefreshToken_shouldReturnAccessToken() throws Exception {
        // given
        String accessTokenJson = "{\"access_token\": \"win-access-token\", \"expires_in\": 7200}";
        JsonNode jsonNode = objectMapper.readTree(accessTokenJson);

        // when
        AccessToken accessToken = winTokenJsonObjectMapper.mapAccessTokenJsonNodeToRefreshToken(jsonNode);

        // should
        assertEquals("win-access-token", accessToken.getToken());
        assertEquals(7200, accessToken.getExpiresIn());
    }
}
