package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.exception.NoRecordOfRefreshTokenException;
import com.iliauni.usersyncglobalservice.exception.RefreshTokenIsNullException;
import com.iliauni.usersyncglobalservice.model.Oauth2Client;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.repository.RefreshTokenRepository;
import com.iliauni.usersyncglobalservice.token.TokenRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class RefreshTokenService<T extends Oauth2Client> {
    private RefreshTokenRepository repository;
    TokenRetriever<T> tokenRetriever;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository repository, TokenRetriever<T> tokenRetriever) {
        this.repository = repository;
        this.tokenRetriever = tokenRetriever;
    }

    public RefreshToken generateAndSave(T client, String tokenEndpointUrl) {
        return generate(client, tokenEndpointUrl).map(token -> repository.save(token))
                .orElseThrow(() -> new RefreshTokenIsNullException("Refresh token is null"));
    }

    private Optional<RefreshToken> generate(T client, String tokenEndpointUrl) {
        return Optional.of(tokenRetriever.retrieveRefreshToken(client, tokenEndpointUrl));
    }

    public RefreshToken save(Optional<RefreshToken> optionalToken) {
        return optionalToken.map(token -> repository.save(token))
                .orElseThrow(() -> new RefreshTokenIsNullException("Refresh token is null"));
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void deleteByDateBetween(
            Date creationDateFrom,
            Date creationDateTo) {
        repository.deleteRefreshTokenByCreationDateBetween(
                creationDateFrom,
                creationDateTo);
    }

    public RefreshToken findLatest() {
        try {
            return repository.findFirstByOrderByCreationDateDesc()
                    .orElseThrow(() -> new RefreshTokenIsNullException("Refresh token is null"));
        } catch (RefreshTokenIsNullException exception) {
            throw new NoRecordOfRefreshTokenException(
                    "There is no record of the refresh token in the database: " + exception.getMessage()
            );
        }
    }

    public RefreshToken findByCreationDate(Date creationDate) {
        try {
            return repository.findRefreshTokenByCreationDate(creationDate)
                    .orElseThrow(() -> new RefreshTokenIsNullException("Refresh token is null"));
        } catch (RefreshTokenIsNullException exception) {
            throw new NoRecordOfRefreshTokenException(
                    "There is no record of refresh token with creation date " +
                            creationDate + " in the database: " + exception.getMessage()
            );
        }
    }
}
