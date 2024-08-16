package com.iliauni.idpsyncservice.service;

import com.iliauni.idpsyncservice.exception.RefreshTokenIsNullException;
import com.iliauni.idpsyncservice.model.Oauth2Client;
import com.iliauni.idpsyncservice.model.RefreshToken;
import com.iliauni.idpsyncservice.repository.RefreshTokenRepository;
import com.iliauni.idpsyncservice.token.TokenManager;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
