package com.iliauni.usersyncglobalservice.unit.token;

import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.token.KcTokenExtractor;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KcTokenExtractorTest {

    @Test
    public void extractRefreshTokenFromJsonMap_WhenGivenJsonMap_ShouldReturnRefreshToken()
            throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("refresh_token", "test-refresh-token");
        jsonMap.put("refresh_expires_in", 3600);

        RefreshToken refreshToken = new RefreshToken(
                (String) jsonMap.get("refresh_token"),
                (int) jsonMap.get("refresh_expires_in"));

        KcTokenExtractor kcTokenExtractor = new KcTokenExtractor();
        assertEquals(refreshToken.getToken(), kcTokenExtractor.extractRefreshTokenFromJsonMap(jsonMap).getToken());
        assertEquals(refreshToken.getExpiresIn(), kcTokenExtractor.extractRefreshTokenFromJsonMap(jsonMap).getExpiresIn());
    }

    @Test
    public void extractAccessTokenFromJsonMap_WhenGivenJsonMap_ShouldReturnAccessToken()
            throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("access_token", "test-access-token");
        jsonMap.put("expires_in", 3600);

        AccessToken accessToken = new AccessToken(
                (String) jsonMap.get("access_token"),
                (int) jsonMap.get("expires_in"));

        KcTokenExtractor kcTokenExtractor = new KcTokenExtractor();
        assertEquals(accessToken.getToken(), kcTokenExtractor.extractAccessTokenFromJsonMap(jsonMap).getToken());
        assertEquals(accessToken.getExpiresIn(), kcTokenExtractor.extractAccessTokenFromJsonMap(jsonMap).getExpiresIn());
    }
}
