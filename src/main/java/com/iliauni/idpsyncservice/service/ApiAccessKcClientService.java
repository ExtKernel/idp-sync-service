package com.iliauni.idpsyncservice.service;

import com.iliauni.idpsyncservice.model.ApiAccessKcClient;
import com.iliauni.idpsyncservice.token.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ApiAccessKcClientService extends GenericOauth2ClientService<ApiAccessKcClient> {

    @Autowired
    public ApiAccessKcClientService(
            JpaRepository<ApiAccessKcClient, String> repository,
            CacheService cacheService,
            RefreshTokenService<ApiAccessKcClient> refreshTokenService,
            @Lazy TokenManager<ApiAccessKcClient> tokenManager
    ) {
        super(
                repository,
                cacheService,
                refreshTokenService,
                tokenManager
        );
    }
}
