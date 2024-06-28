package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.model.WinClient;
import com.iliauni.usersyncglobalservice.token.TokenRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class WinClientService extends GenericOauth2ClientService<WinClient> {

    @Autowired
    public WinClientService(
            JpaRepository<WinClient, String> repository,
            RefreshTokenService<WinClient> refreshTokenService,
            TokenRetriever<WinClient> tokenRetriever
    ) {
        super(
                repository,
                refreshTokenService,
                tokenRetriever
        );
    }
}
