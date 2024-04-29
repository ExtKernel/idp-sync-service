package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.exception.ClientIsNullException;
import com.iliauni.usersyncglobalservice.exception.ClientNotFoundException;
import com.iliauni.usersyncglobalservice.exception.NoRecordOfClientsException;
import com.iliauni.usersyncglobalservice.exception.NoRecordOfRefreshTokenForTheClientException;
import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.ApiAccessKcClient;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.repository.ApiAccessKcClientRepository;
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
public class ApiAccessKcClientService implements Oauth2ClientService<ApiAccessKcClient> {
    private final ApiAccessKcClientRepository repository;
    private final RefreshTokenService<ApiAccessKcClient> refreshTokenService;
    private final TokenRetriever<ApiAccessKcClient> tokenRetriever;

    @Autowired
    public ApiAccessKcClientService(
            ApiAccessKcClientRepository repository,
            RefreshTokenService<ApiAccessKcClient> refreshTokenService,
            @Lazy TokenRetriever<ApiAccessKcClient> tokenRetriever
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
    public RefreshToken getRefreshToken(String clientId, String tokenEndpointUrl) {
        ApiAccessKcClient client = findById(clientId);
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
        ApiAccessKcClient client = findById(clientId);
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
                    "There is no record of refresh token for Keycloak API access client with id: " + findById(id).getId());
        }
    }

    @Transactional
    @Override
    public ApiAccessKcClient save(Optional<ApiAccessKcClient> optionalClient) throws ClientIsNullException {
        return optionalClient.map(repository::save)
                .orElseThrow(() -> new ClientIsNullException("Keycloak API access client is null"));
    }

    @Override
    public List<ApiAccessKcClient> findAll() {
        List<ApiAccessKcClient> clients = repository.findAll();

        if (!clients.isEmpty()) {
            return clients;
        } else {
            throw new NoRecordOfClientsException("There is no record of Keycloak API access clients in the database");
        }
    }

    @Override
    public ApiAccessKcClient findById(String id) throws ClientNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Keycloak API access client with id " + id + " was not found"));
    }

    @Override
    public ApiAccessKcClient update(Optional<ApiAccessKcClient> optionalClient) throws ClientIsNullException {
        return optionalClient.map(repository::save)
                .orElseThrow(() -> new ClientIsNullException("Keycloak API access client is null"));
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
