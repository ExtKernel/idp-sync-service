package com.iliauni.idpsyncservice.token;

import com.fasterxml.jackson.databind.JsonNode;
import com.iliauni.idpsyncservice.model.AccessToken;
import com.iliauni.idpsyncservice.model.RefreshToken;

public abstract class GenericTokenJsonObjectMapper implements TokenJsonObjectMapper {
    @Override
    public RefreshToken mapRefreshTokenJsonNodeToRefreshToken(JsonNode refreshTokenJsonNode) {
        return new RefreshToken(
                refreshTokenJsonNode.path("refresh_token").asText(),
                refreshTokenJsonNode.path("refresh_expires_in").asInt()
        );
    }

    @Override
    public AccessToken mapAccessTokenJsonNodeToRefreshToken(JsonNode accessTokenJsonNode) {
        return new AccessToken(
                accessTokenJsonNode.path("access_token").asText(),
                accessTokenJsonNode.path("expires_in").asInt()
        );
    }
}
