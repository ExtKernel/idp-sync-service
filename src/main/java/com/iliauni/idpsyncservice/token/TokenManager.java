package com.iliauni.idpsyncservice.token;

import com.iliauni.idpsyncservice.model.AccessToken;
import com.iliauni.idpsyncservice.model.Client;
import com.iliauni.idpsyncservice.model.RefreshToken;

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
