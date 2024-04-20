package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.exception.ClientIsNullException;
import com.iliauni.usersyncglobalservice.exception.ClientNotFoundException;
import com.iliauni.usersyncglobalservice.exception.NoRecordOfClientsException;
import com.iliauni.usersyncglobalservice.exception.NoRecordOfRefreshTokenForTheClientException;
import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.KcClient;

import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.model.WinClient;
import com.iliauni.usersyncglobalservice.repository.KcClientRepository;
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
public class KcClientService implements Oauth2ClientService<KcClient> {
    private final KcClientRepository repository;
    private final RefreshTokenService<KcClient> refreshTokenService;
    private final TokenRetriever<KcClient> tokenRetriever;

    @Autowired
    public KcClientService(
            KcClientRepository repository,
            RefreshTokenService<KcClient> refreshTokenService,
            @Lazy TokenRetriever<KcClient> tokenRetriever
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
        KcClient client = findById(clientId);
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
        KcClient client = findById(clientId);
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
                    "There is no record of refresh token for KC client with id: " + findById(id).getId());
        }
    }

    @Transactional
    @Override
    public KcClient save(Optional<KcClient> optionalClient) throws ClientIsNullException {
        return optionalClient.map(repository::save)
                .orElseThrow(() -> new ClientIsNullException("KC client is null"));
    }

    @Override
    public List<KcClient> findAll() {
        List<KcClient> clients = repository.findAll();

        if (!clients.isEmpty()) {
            return clients;
        } else {
            throw new NoRecordOfClientsException("There is no record of KC clients in the database");
        }
    }

    @Override
    public KcClient findById(String id) throws ClientNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("KC client with id " + id + " was not found"));
    }

    @Override
    public KcClient update(Optional<KcClient> optionalClient) throws ClientIsNullException {
        return optionalClient.map(repository::save)
                .orElseThrow(() -> new ClientIsNullException("KC client is null"));
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
