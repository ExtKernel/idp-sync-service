package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.RefreshToken;

import java.util.Map;

public interface TokenExtractor {

    /**
     * Extracts the refresh token and its expiration time
     * from the JSON map and constructs a RefreshToken object.
     *
     * @param jsonMap A JSON map
     *                containing the refresh token and its expiration time.
     * @return A RefreshToken object
     * initialized with the extracted refresh token and expiration time.
     */
    RefreshToken extractRefreshTokenFromJsonMap(
            Map<?, ?> jsonMap
    );

    /**
     * Extracts the access token and its expiration time
     * from the JSON map and constructs a AccessToken object.
     *
     * @param jsonMap A JSON map
     *                containing the access token and its expiration time.
     * @return A AccessToken object
     * initialized with the extracted access token and expiration time.
     */
    AccessToken extractAccessTokenFromJsonMap(
            Map<?, ?> jsonMap
    );
}
