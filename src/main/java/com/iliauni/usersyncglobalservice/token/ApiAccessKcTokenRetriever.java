package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.ApiAccessKcClient;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiAccessKcTokenRetriever implements TokenRetriever<ApiAccessKcClient> {
    private final TokenRequestSender<ApiAccessKcClient> requestSender;

    @Autowired
    public ApiAccessKcTokenRetriever(
            TokenRequestSender<ApiAccessKcClient> requestSender
    ) {
        this.requestSender = requestSender;
    }

    @Override
    public AccessToken retrieveAccessToken(
            ApiAccessKcClient client,
            String tokenEndpointUrl
    ) {
        return requestSender.getAccessTokenByRefreshToken(
                client,
                tokenEndpointUrl
        );
    }

    @Override
    public RefreshToken retrieveRefreshToken(
            ApiAccessKcClient client,
            String tokenEndpointUrl
    ) {
        return requestSender.getRefreshToken(
                client,
                tokenEndpointUrl
        );
    }
}
