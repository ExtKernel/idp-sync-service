package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.model.ApiAccessKcClient;
import com.iliauni.usersyncglobalservice.token.TokenRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ApiAccessKcClientService extends GenericOauth2ClientService<ApiAccessKcClient> {

    @Autowired
    public ApiAccessKcClientService(
            JpaRepository<ApiAccessKcClient, String> repository,
            RefreshTokenService<ApiAccessKcClient> refreshTokenService,
            TokenRetriever<ApiAccessKcClient> tokenRetriever
    ) {
        super(
                repository,
                refreshTokenService,
                tokenRetriever
        );
    }
}
