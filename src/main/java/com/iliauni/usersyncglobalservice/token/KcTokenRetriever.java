package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.exception.NoRecordOfRefreshTokenForTheClientException;
import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.KcClient;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.service.KcClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class KcTokenRetriever<T extends KcClient> implements TokenRetriever<T> {
    @Lazy
    KcClientService clientService;

    TokenRequestSender<T> requestSender;

    @Autowired
    public KcTokenRetriever(KcClientService clientService, TokenRequestSender<T> requestSender) {
        this.clientService = clientService;
        this.requestSender = requestSender;
    }

    @Override
    public AccessToken retrieveAccessToken(
            T client,
            String tokenEndpointUrl) {
        try {
            clientService.getLatestRefreshToken(client.getId());
        } catch (NoRecordOfRefreshTokenForTheClientException exception) {
            clientService.generateAndSaveRefreshToken(client.getId());
        }

        return requestSender.getAccessTokenByRefreshToken(client, tokenEndpointUrl);
    }

    @Override
    public RefreshToken retrieveRefreshToken(
            T client,
            String tokenEndpointUrl) {
        return requestSender.getRefreshToken(client, tokenEndpointUrl);
    }
}
