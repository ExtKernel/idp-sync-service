package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.model.WinClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WinTokenRetriever implements TokenRetriever<WinClient> {
    private final TokenRequestSender<WinClient> requestSender;

    @Autowired
    public WinTokenRetriever(
            TokenRequestSender<WinClient> requestSender
    ) {
        this.requestSender = requestSender;
    }

    @Override
    public AccessToken retrieveAccessToken(
            WinClient client,
            String tokenEndpointUrl
    ) {
        return requestSender.getAccessTokenByRefreshToken(client, tokenEndpointUrl);
    }

    @Override
    public RefreshToken retrieveRefreshToken(
            WinClient client,
            String tokenEndpointUrl
    ) {
        return requestSender.getRefreshToken(client, tokenEndpointUrl);
    }
}
