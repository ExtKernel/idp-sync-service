package com.iliauni.idpsyncservice.token;

import com.fasterxml.jackson.databind.JsonNode;
import com.iliauni.idpsyncservice.model.AccessToken;
import com.iliauni.idpsyncservice.model.RefreshToken;

public interface TokenJsonObjectMapper {

    /**
     * Maps a {@link JsonNode} representation of a refresh token, retrieved from a request to a {@link RefreshToken} object representation.
     *
     * @param refreshTokenJsonNode a {@link JsonNode} representation of the refresh token.
     * @return the {@link RefreshToken} object.
     */
    RefreshToken mapRefreshTokenJsonNodeToRefreshToken(
            JsonNode refreshTokenJsonNode
    );

    /**
     * Maps a {@link JsonNode} representation of an access token, retrieved from a request to a {@link AccessToken} object representation.
     *
     * @param accessTokenJsonNode a {@link JsonNode} representation of the access token.
     * @return the {@link AccessToken} object.
     */
    AccessToken mapAccessTokenJsonNodeToRefreshToken(
            JsonNode accessTokenJsonNode
    );
}
