package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.exception.ClientIsNullException;
import com.iliauni.usersyncglobalservice.exception.ClientNotFoundException;
import com.iliauni.usersyncglobalservice.exception.NoRecordOfClientsException;
import com.iliauni.usersyncglobalservice.exception.NoRecordOfRefreshTokenForTheClientException;
import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.model.WinClient;
import com.iliauni.usersyncglobalservice.repository.WinClientRepository;
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
public class WinClientService implements Oauth2ClientService<WinClient> {
    private final WinClientRepository repository;
    private final RefreshTokenService<WinClient> refreshTokenService;
    private final TokenRetriever<WinClient> tokenRetriever;

    @Autowired
    public WinClientService(
            WinClientRepository repository,
            RefreshTokenService<WinClient> refreshTokenService,
            @Lazy TokenRetriever<WinClient> tokenRetriever
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
        WinClient client = findById(clientId);
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
        WinClient client = findById(clientId);
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
    public RefreshToken getLatestRefreshToken(String clientId) throws NoRecordOfRefreshTokenForTheClientException {
        if (!findById(clientId).getRefreshTokens().isEmpty()) {
            List<RefreshToken> refreshTokens = findById(clientId).getRefreshTokens();

            return refreshTokens.get(refreshTokens.size() - 1);
        } else {
            throw new NoRecordOfRefreshTokenForTheClientException(
                    "There is no record of refresh token for Windows client with id: " + findById(clientId).getId());
        }
    }

    @Transactional
    @Override
    public WinClient save(Optional<WinClient> optionalClient) throws ClientIsNullException {
        return optionalClient.map(repository::save)
                .orElseThrow(() -> new ClientIsNullException("Windows client is null"));
    }

    @Override
    public List<WinClient> findAll() throws NoRecordOfClientsException {
        List<WinClient> clients = repository.findAll();

        if (!clients.isEmpty()) {
            return clients;
        } else {
            throw new NoRecordOfClientsException("There is no record of Windows clients in the database");
        }
    }

    @Override
    public WinClient findById(String id) throws ClientNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Windows client with id " + id + " was not found"));
    }

    @Override
    public WinClient update(Optional<WinClient> optionalClient) throws ClientIsNullException {
        return optionalClient.map(repository::save)
                .orElseThrow(() -> new ClientIsNullException("Windows client is null"));
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
