package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.model.KcClient;

import com.iliauni.usersyncglobalservice.repository.ClientRepository;
import com.iliauni.usersyncglobalservice.token.TokenRetriever;
import org.springframework.stereotype.Service;

@Service
public class KcClientService<T extends KcClient> extends Oauth2ClientService<T> {
    public KcClientService(ClientRepository<T> repository, RefreshTokenService<T> refreshTokenService, TokenRetriever<T> tokenRetriever) {
        super(repository, refreshTokenService, tokenRetriever);
    }
}
