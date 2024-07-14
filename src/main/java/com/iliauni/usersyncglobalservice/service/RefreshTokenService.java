package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.exception.RefreshTokenIsNullException;
import com.iliauni.usersyncglobalservice.model.Oauth2Client;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.repository.RefreshTokenRepository;
import com.iliauni.usersyncglobalservice.token.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RefreshTokenService<T extends Oauth2Client> extends GenericCrudService<RefreshToken, Long> {
    private final RefreshTokenRepository repository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public RefreshToken generateAndSave(
            T client,
            String tokenEndpointUrl,
            TokenManager tokenManager
    ) {
        return generate(
                client,
                tokenEndpointUrl,
                tokenManager
        ).map(repository::save)
                .orElseThrow(() -> new RefreshTokenIsNullException("Refresh token is null"));
    }

    private Optional<RefreshToken> generate(
            T client,
            String tokenEndpointUrl,
            TokenManager tokenRetriever
            ) {
        return Optional.of(tokenRetriever.getRefreshToken(client, tokenEndpointUrl));
    }
}
