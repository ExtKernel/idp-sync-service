package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.Client;
import com.iliauni.usersyncglobalservice.model.RefreshToken;

public interface TokenManager<T extends Client> {
    AccessToken getAccessToken(
            T client,
            String tokenEndpointUrl,
            RefreshToken refreshToken
    );
    RefreshToken getRefreshToken(
            T client,
            String tokenEndpointUrl
    );
}
