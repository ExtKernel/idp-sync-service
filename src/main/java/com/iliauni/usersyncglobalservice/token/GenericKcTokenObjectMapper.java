package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GenericKcTokenObjectMapper implements TokenObjectMapper {

    @Override
    public RefreshToken mapRefreshTokenJsonMapToRefreshToken(Map<String, Object> jsonMap) {
        return new RefreshToken(
                (String) jsonMap.get("refresh_token"),
                (int) jsonMap.get("refresh_expires_in"));
    }

    @Override
    public AccessToken mapAccessTokenJsonMapToRefreshToken(Map<String, Object> jsonMap) {
        return new AccessToken(
                (String) jsonMap.get("access_token"),
                (int) jsonMap.get("expires_in"));    }
}
