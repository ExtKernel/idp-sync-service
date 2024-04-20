package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.Oauth2Client;
import com.iliauni.usersyncglobalservice.model.RefreshToken;

public interface TokenRequestSender<T extends Oauth2Client> {
    AccessToken getAccessTokenByCredentials(
            T client,
            String tokenEndpointUrl);
    AccessToken getAccessTokenByRefreshToken(
            T client,
            String tokenEndpointUrl);
    RefreshToken getRefreshToken(
            T client,
            String tokenEndpointUrl);

}
