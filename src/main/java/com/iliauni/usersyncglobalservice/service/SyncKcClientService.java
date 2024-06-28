package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.model.SyncKcClient;
import com.iliauni.usersyncglobalservice.token.TokenRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class SyncKcClientService extends GenericOauth2ClientService<SyncKcClient> {

    @Autowired
    public SyncKcClientService(
            JpaRepository<SyncKcClient, String> repository,
            RefreshTokenService<SyncKcClient> refreshTokenService,
            TokenRetriever<SyncKcClient> tokenRetriever
    ) {
        super(
                repository,
                refreshTokenService,
                tokenRetriever
        );
    }
}
