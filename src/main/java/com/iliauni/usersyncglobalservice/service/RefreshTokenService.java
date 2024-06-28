package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.exception.NoRecordOfRefreshTokenException;
import com.iliauni.usersyncglobalservice.exception.RefreshTokenIsNullException;
import com.iliauni.usersyncglobalservice.model.Oauth2Client;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.repository.RefreshTokenRepository;
import com.iliauni.usersyncglobalservice.token.TokenRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class RefreshTokenService<T extends Oauth2Client> {
    private final RefreshTokenRepository repository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    public RefreshToken generateAndSave(
            T client,
            String tokenEndpointUrl,
            TokenRetriever tokenRetriever
    ) {
        return generate(
                client,
                tokenEndpointUrl,
                tokenRetriever
        ).map(repository::save)
                .orElseThrow(() -> new RefreshTokenIsNullException("Refresh token is null"));
    }

    private Optional<RefreshToken> generate(
            T client,
            String tokenEndpointUrl,
            TokenRetriever tokenRetriever
            ) {
        return Optional.of(tokenRetriever.retrieveRefreshToken(client, tokenEndpointUrl));
    }

    @Transactional
    public RefreshToken save(Optional<RefreshToken> optionalToken) {
        return optionalToken.map(repository::save)
                .orElseThrow(() -> new RefreshTokenIsNullException("Refresh token is null"));
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional
    public void deleteByDateBetween(
            Date creationDateFrom,
            Date creationDateTo) {
        repository.deleteRefreshTokenByCreationDateBetween(
                creationDateFrom,
                creationDateTo);
    }

    public RefreshToken findLatest() throws NoRecordOfRefreshTokenException {
        try {
            return repository.findFirstByOrderByCreationDateDesc()
                    .orElseThrow(() -> new RefreshTokenIsNullException("Refresh token is null"));
        } catch (RefreshTokenIsNullException exception) {
            throw new NoRecordOfRefreshTokenException(
                    "There is no record of the refresh token in the database: " + exception.getMessage()
            );
        }
    }

    public RefreshToken findByCreationDate(Date creationDate) throws NoRecordOfRefreshTokenException {
        try {
            return repository.findRefreshTokenByCreationDate(creationDate)
                    .orElseThrow(() -> new RefreshTokenIsNullException("Refresh token is null"));
        } catch (RefreshTokenIsNullException exception) {
            throw new NoRecordOfRefreshTokenException(
                    "There is no record of refresh token with creation date " +
                            creationDate + " in the database: " + exception.getMessage(),
                    exception
            );
        }
    }
}
