package com.iliauni.idpsyncservice.service;

import com.iliauni.idpsyncservice.exception.NoRecordOfRefreshTokenForTheClientException;
import com.iliauni.idpsyncservice.model.AccessToken;
import com.iliauni.idpsyncservice.model.Oauth2Client;
import com.iliauni.idpsyncservice.model.RefreshToken;

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
