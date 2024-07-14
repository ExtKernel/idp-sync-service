package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.Oauth2Client;
import com.iliauni.usersyncglobalservice.model.RefreshToken;

public abstract class GenericTokenManager<T extends Oauth2Client> implements TokenManager<T> {
    private final TokenJsonObjectMapper jsonObjectMapper;
    private final TokenRequestSender<T> requestSender;

    public GenericTokenManager(
            TokenJsonObjectMapper jsonObjectMapper,
            TokenRequestSender<T> requestSender
    ) {
        this.jsonObjectMapper = jsonObjectMapper;
        this.requestSender = requestSender;
    }

    @Override
    public AccessToken getAccessToken(
            T client,
            String tokenEndpointUrl,
            RefreshToken refreshToken
    ) {
        return jsonObjectMapper.mapAccessTokenJsonNodeToRefreshToken(requestSender.getAccessToken(
                client,
                tokenEndpointUrl,
                refreshToken
        ));
    }

    @Override
    public RefreshToken getRefreshToken(
            T client,
            String tokenEndpointUrl
    ) {
        return jsonObjectMapper.mapRefreshTokenJsonNodeToRefreshToken(requestSender.getRefreshToken(
                client,
                tokenEndpointUrl
        ));
    }
}
