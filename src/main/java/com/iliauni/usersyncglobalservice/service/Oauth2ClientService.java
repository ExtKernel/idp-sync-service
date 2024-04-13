package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.exception.NoRecordOfRefreshTokenForTheClientException;
import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.Oauth2Client;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.repository.ClientRepository;
import com.iliauni.usersyncglobalservice.token.TokenRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class Oauth2ClientService<T extends Oauth2Client> extends ClientService<T> {
    @Lazy
    private final RefreshTokenService<T> refreshTokenService;

    @Lazy
    private final TokenRetriever<T> tokenRetriever;

    @Value("${kcTokenEndpointUrl}")
    private String tokenEndpointUrl;

    @Autowired
    public Oauth2ClientService(ClientRepository<T> repository, RefreshTokenService<T> refreshTokenService, TokenRetriever<T> tokenRetriever) {
        super(repository);
        this.refreshTokenService = refreshTokenService;
        this.tokenRetriever = tokenRetriever;
    }

    public AccessToken generateAccessToken(String clientId) {
        return tokenRetriever.retrieveAccessToken(findById(clientId), tokenEndpointUrl);
    }

    public RefreshToken generateAndSaveRefreshToken(String clientId) {
        T client = findById(clientId);
        List<RefreshToken> refreshTokens;

        if (client.getRefreshTokens() != null) {
            refreshTokens = client.getRefreshTokens();
        } else {
            refreshTokens = new ArrayList<>();
        }

        RefreshToken refreshToken = refreshTokenService.generateAndSave(client, tokenEndpointUrl);
        refreshTokens.add(refreshToken);

        client.setRefreshTokens(refreshTokens);
        update(Optional.of(client));

        return refreshToken;
    }

    public RefreshToken getLatestRefreshToken(String clientId) {
        if (findById(clientId).getRefreshTokens() != null) {
            List<RefreshToken> refreshTokens = findById(clientId).getRefreshTokens();

            return refreshTokens.get(refreshTokens.size() - 1);
        } else {
            throw new NoRecordOfRefreshTokenForTheClientException(
                    "There is no record of refresh token for client: " + findById(clientId).getId());
        }
    }
}
