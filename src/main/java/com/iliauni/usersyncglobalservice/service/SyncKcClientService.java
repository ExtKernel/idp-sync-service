package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.exception.ClientIsNullException;
import com.iliauni.usersyncglobalservice.exception.ClientNotFoundException;
import com.iliauni.usersyncglobalservice.exception.NoRecordOfClientsException;
import com.iliauni.usersyncglobalservice.exception.NoRecordOfRefreshTokenForTheClientException;
import com.iliauni.usersyncglobalservice.model.AccessToken;

import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.model.SyncKcClient;
import com.iliauni.usersyncglobalservice.repository.SyncKcClientRepository;
import com.iliauni.usersyncglobalservice.token.TokenRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SyncKcClientService implements Oauth2ClientService<SyncKcClient> {
    private final SyncKcClientRepository repository;
    private final RefreshTokenService<SyncKcClient> refreshTokenService;
    private final TokenRetriever<SyncKcClient> tokenRetriever;

    @Autowired
    public SyncKcClientService(
            SyncKcClientRepository repository,
            RefreshTokenService<SyncKcClient> refreshTokenService,
            @Lazy TokenRetriever<SyncKcClient> tokenRetriever
    ) {
        this.repository = repository;
        this.refreshTokenService = refreshTokenService;
        this.tokenRetriever = tokenRetriever;
    }

    @Override
    public AccessToken generateAccessToken(
            String clientId,
            String tokenEndpointUrl
    ) {
        return tokenRetriever.retrieveAccessToken(findById(clientId), tokenEndpointUrl);
    }

    @Override
    public RefreshToken getRefreshToken(
            String clientId,
            String tokenEndpointUrl
    ) {
        SyncKcClient client = findById(clientId);
        try {
            RefreshToken refreshToken = getLatestRefreshToken(client.getId());
            Date refreshTokenExpirationDate = Date.from(Instant.ofEpochMilli(refreshToken.getCreationDate().getTime() + refreshToken.getExpiresIn()));

            if (!refreshTokenExpirationDate.after(new Date())) {
                return generateAndSaveRefreshToken(client.getId(), tokenEndpointUrl);
            } else {
                return refreshToken;
            }
        } catch (NoRecordOfRefreshTokenForTheClientException exception) {
            return generateAndSaveRefreshToken(client.getId(), tokenEndpointUrl);
        }
    }

    @Override
    public RefreshToken generateAndSaveRefreshToken(
            String clientId,
            String tokenEndpointUrl
    ) {
        SyncKcClient client = findById(clientId);
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
    public RefreshToken getLatestRefreshToken(String id) {
        if (!findById(id).getRefreshTokens().isEmpty()) {
            List<RefreshToken> refreshTokens = findById(id).getRefreshTokens();

            return refreshTokens.get(refreshTokens.size() - 1);
        } else {
            throw new NoRecordOfRefreshTokenForTheClientException(
                    "There is no record of refresh token for Keycloak client with id: " + findById(id).getId());
        }
    }

    @Transactional
    @Override
    public SyncKcClient save(Optional<SyncKcClient> optionalClient) throws ClientIsNullException {
        return optionalClient.map(repository::save)
                .orElseThrow(() -> new ClientIsNullException("Keycloak client is null"));
    }

    @Override
    public List<SyncKcClient> findAll() {
        List<SyncKcClient> clients = repository.findAll();

        if (!clients.isEmpty()) {
            return clients;
        } else {
            throw new NoRecordOfClientsException("There is no record of Keycloak clients in the database");
        }
    }

    @Override
    public SyncKcClient findById(String id) throws ClientNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Keycloak client with id " + id + " was not found"));
    }

    @Override
    public SyncKcClient update(Optional<SyncKcClient> optionalClient) throws ClientIsNullException {
        return optionalClient.map(repository::save)
                .orElseThrow(() -> new ClientIsNullException("Keycloak client is null"));
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
