package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.exception.NoRecordOfRefreshTokenForTheClientException;
import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.Oauth2Client;
import com.iliauni.usersyncglobalservice.model.RefreshToken;

public interface Oauth2ClientService<T extends Oauth2Client> extends ClientService<T> {

    AccessToken generateAccessToken(
            String clientId,
            String tokenEndpointUrl
    );
    RefreshToken getRefreshToken(
            String clientId,
            String tokenEndpointUrl
    );
    RefreshToken generateAndSaveRefreshToken(
            String clientId,
            String tokenEndpointUrl
    );
    RefreshToken getLatestRefreshToken(String clientId)
            throws NoRecordOfRefreshTokenForTheClientException;
}
