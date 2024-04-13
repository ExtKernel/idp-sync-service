package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.Oauth2Client;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import org.springframework.lang.Nullable;

public interface TokenRetriever<T extends Oauth2Client> {
    AccessToken retrieveAccessToken(
            T client,
            @Nullable String tokenEndpointUrl);

    RefreshToken retrieveRefreshToken(
            T client,
            @Nullable String tokenEndpointUrl);
}
