package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.exception.NoRecordOfRefreshTokenForTheClientException;
import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.Oauth2Client;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.token.TokenRetriever;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class GenericOauth2ClientService<T extends Oauth2Client> extends GenericClientService<T> implements Oauth2ClientService<T> {
    private final RefreshTokenService<T> refreshTokenService;
    private final TokenRetriever<T> tokenRetriever;

    public GenericOauth2ClientService(
            JpaRepository<T, String> repository,
            RefreshTokenService<T> refreshTokenService,
            TokenRetriever<T> tokenRetriever
    ) {
        super(repository);
        this.refreshTokenService = refreshTokenService;
        this.tokenRetriever = tokenRetriever;
    }

    @Override
    public AccessToken generateAccessToken(
            String clientId,
            String tokenEndpointUrl
    ) {
        return tokenRetriever.retrieveAccessToken(
                findById(clientId),
                tokenEndpointUrl
        );
    }

    @Override
    public RefreshToken getRefreshToken(
            String clientId,
            String tokenEndpointUrl
    ) {
        T client = findById(clientId);
        try {
            RefreshToken refreshToken = getLatestRefreshToken(client.getId());
            Date refreshTokenExpirationDate = Date.from(Instant.ofEpochMilli(
                    refreshToken.getCreationDate().getTime() + refreshToken.getExpiresIn()));

            if (!refreshTokenExpirationDate.after(new Date())) {
                return generateAndSaveRefreshToken(
                        client.getId(),
                        tokenEndpointUrl
                );
            } else {
                return refreshToken;
            }
        } catch (NoRecordOfRefreshTokenForTheClientException exception) {
            return generateAndSaveRefreshToken(
                    client.getId(),
                    tokenEndpointUrl
            );
        }
    }

    @Override
    public RefreshToken generateAndSaveRefreshToken(
            String clientId,
            String tokenEndpointUrl
    ) {
        T client = findById(clientId);
        List<RefreshToken> refreshTokens;

        if (client.getRefreshTokens() != null) {
            refreshTokens = client.getRefreshTokens();
        } else {
            refreshTokens = new ArrayList<>();
        }

        RefreshToken refreshToken = refreshTokenService.generateAndSave(
                client,
                tokenEndpointUrl,
                tokenRetriever
        );
        refreshTokens.add(refreshToken);

        client.setRefreshTokens(refreshTokens);
        update(Optional.of(client));

        return refreshToken;
    }

    @Override
    public RefreshToken getLatestRefreshToken(String clientId)
            throws NoRecordOfRefreshTokenForTheClientException {
            if (!findById(clientId).getRefreshTokens().isEmpty()) {
                List<RefreshToken> refreshTokens = findById(clientId).getRefreshTokens();

                return refreshTokens.get(refreshTokens.size() - 1);
            } else {
                throw new NoRecordOfRefreshTokenForTheClientException(
                        "There is no record of refresh tokens for the client with id: "
                                + findById(clientId).getId());
            }
    }
}
