package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KcTokenExtractor implements TokenExtractor {
    public RefreshToken extractRefreshTokenFromJsonMap(
            Map<?, ?> jsonMap
    ) {
        return new RefreshToken(
                (String) jsonMap.get("refresh_token"),
                (int) jsonMap.get("refresh_expires_in"));
    }

    public AccessToken extractAccessTokenFromJsonMap(
            Map<?, ?> jsonMap
    ) {
        return new AccessToken(
                (String) jsonMap.get("access_token"),
                (int) jsonMap.get("expires_in"));
    }
}
