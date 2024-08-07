package com.iliauni.idpsyncservice.service;

import com.iliauni.idpsyncservice.model.SyncKcClient;
import com.iliauni.idpsyncservice.token.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class SyncKcClientService extends GenericOauth2ClientService<SyncKcClient> {

    @Autowired
    public SyncKcClientService(
            JpaRepository<SyncKcClient, String> repository,
            CacheService cacheService,
            RefreshTokenService<SyncKcClient> refreshTokenService,
            @Lazy TokenManager<SyncKcClient> tokenManager
    ) {
        super(
                repository,
                cacheService,
                refreshTokenService,
                tokenManager
        );
    }
}
