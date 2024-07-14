package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.model.WinClient;
import com.iliauni.usersyncglobalservice.token.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class WinClientService extends GenericOauth2ClientService<WinClient> {

    @Autowired
    public WinClientService(
            JpaRepository<WinClient, String> repository,
            RefreshTokenService<WinClient> refreshTokenService,
            @Lazy TokenManager<WinClient> tokenManager
    ) {
        super(
                repository,
                refreshTokenService,
                tokenManager
        );
    }
}
