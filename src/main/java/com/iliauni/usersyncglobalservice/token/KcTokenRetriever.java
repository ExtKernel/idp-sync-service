package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.KcClient;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KcTokenRetriever implements TokenRetriever<KcClient> {
    private final TokenRequestSender<KcClient> requestSender;

    @Autowired
    public KcTokenRetriever(
            TokenRequestSender<KcClient> requestSender
    ) {
        this.requestSender = requestSender;
    }

    @Override
    public AccessToken retrieveAccessToken(
            KcClient client,
            String tokenEndpointUrl
    ) {
        return requestSender.getAccessTokenByRefreshToken(client, tokenEndpointUrl);
    }

    @Override
    public RefreshToken retrieveRefreshToken(
            KcClient client,
            String tokenEndpointUrl
    ) {
        return requestSender.getRefreshToken(client, tokenEndpointUrl);
    }
}
