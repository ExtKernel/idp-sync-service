package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.model.SyncKcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SyncKcTokenRetriever implements TokenRetriever<SyncKcClient> {
    private final TokenRequestSender<SyncKcClient> requestSender;

    @Autowired
    public SyncKcTokenRetriever(
            TokenRequestSender<SyncKcClient> requestSender
    ) {
        this.requestSender = requestSender;
    }

    @Override
    public AccessToken retrieveAccessToken(
            SyncKcClient client,
            String tokenEndpointUrl
    ) {
        return requestSender.getAccessTokenByRefreshToken(client, tokenEndpointUrl);
    }

    @Override
    public RefreshToken retrieveRefreshToken(
            SyncKcClient client,
            String tokenEndpointUrl
    ) {
        return requestSender.getRefreshToken(client, tokenEndpointUrl);
    }
}
