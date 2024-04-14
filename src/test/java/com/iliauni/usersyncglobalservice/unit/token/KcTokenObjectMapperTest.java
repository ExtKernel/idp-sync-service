package com.iliauni.usersyncglobalservice.unit.token;

import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.token.KcTokenObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KcTokenObjectMapperTest {

    @Test
    public void mapRefreshTokenJsonMapToRefreshToken_WhenGivenJsonMap_ShouldReturnRefreshToken()
            throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("refresh_token", "test-refresh-token");
        jsonMap.put("refresh_expires_in", 3600);

        RefreshToken refreshToken = new RefreshToken(
                (String) jsonMap.get("refresh_token"),
                (int) jsonMap.get("refresh_expires_in"));

        KcTokenObjectMapper kcTokenExtractor = new KcTokenObjectMapper();
        assertEquals(refreshToken.getToken(), kcTokenExtractor.mapRefreshTokenJsonMapToRefreshToken(jsonMap).getToken());
        assertEquals(refreshToken.getExpiresIn(), kcTokenExtractor.mapRefreshTokenJsonMapToRefreshToken(jsonMap).getExpiresIn());
    }

    @Test
    public void mapAccessTokenJsonMapToRefreshToken_WhenGivenJsonMap_ShouldReturnAccessToken()
            throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("access_token", "test-access-token");
        jsonMap.put("expires_in", 3600);

        AccessToken accessToken = new AccessToken(
                (String) jsonMap.get("access_token"),
                (int) jsonMap.get("expires_in"));

        KcTokenObjectMapper kcTokenExtractor = new KcTokenObjectMapper();
        assertEquals(accessToken.getToken(), kcTokenExtractor.mapAccessTokenJsonMapToRefreshToken(jsonMap).getToken());
        assertEquals(accessToken.getExpiresIn(), kcTokenExtractor.mapAccessTokenJsonMapToRefreshToken(jsonMap).getExpiresIn());
    }
}
