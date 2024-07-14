package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.model.ApiAccessKcClient;
import com.iliauni.usersyncglobalservice.repository.ApiAccessKcClientRepository;
import com.iliauni.usersyncglobalservice.token.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ApiAccessKcClientService extends GenericOauth2ClientService<ApiAccessKcClient> {

    @Autowired
    public ApiAccessKcClientService(
            ApiAccessKcClientRepository repository,
            RefreshTokenService<ApiAccessKcClient> refreshTokenService,
            @Lazy TokenManager<ApiAccessKcClient> tokenManager
    ) {
        super(
                repository,
                refreshTokenService,
                tokenManager
        );
    }
}
