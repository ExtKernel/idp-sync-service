package com.iliauni.usersyncglobalservice.token;

import com.fasterxml.jackson.databind.JsonNode;
import com.iliauni.usersyncglobalservice.model.Oauth2Client;
import com.iliauni.usersyncglobalservice.model.RefreshToken;

public interface TokenRequestSender<T extends Oauth2Client> {
    JsonNode getAccessToken(
            T client,
            String tokenEndpointUrl,
            RefreshToken refreshToken
    );
    JsonNode getRefreshToken(
            T client,
            String tokenEndpointUrl
    );
}
