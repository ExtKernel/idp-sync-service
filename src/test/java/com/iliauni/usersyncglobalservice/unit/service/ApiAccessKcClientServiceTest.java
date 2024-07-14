package com.iliauni.usersyncglobalservice.unit.service;

import com.iliauni.usersyncglobalservice.exception.ModelIsNullException;
import com.iliauni.usersyncglobalservice.exception.ModelNotFoundException;
import com.iliauni.usersyncglobalservice.exception.NoRecordOfRefreshTokenForTheClientException;
import com.iliauni.usersyncglobalservice.model.ApiAccessKcClient;
import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.repository.ApiAccessKcClientRepository;
import com.iliauni.usersyncglobalservice.service.ApiAccessKcClientService;
import com.iliauni.usersyncglobalservice.service.RefreshTokenService;
import com.iliauni.usersyncglobalservice.token.TokenManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiAccessKcClientServiceTest {
    private ApiAccessKcClientRepository repository;
    private RefreshTokenService<ApiAccessKcClient> refreshTokenService;
    private TokenManager<ApiAccessKcClient> tokenManager;
    private ApiAccessKcClientService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ApiAccessKcClientRepository.class);
        refreshTokenService = Mockito.mock(RefreshTokenService.class);
        tokenManager = Mockito.mock(TokenManager.class);
        service = new ApiAccessKcClientService(repository, refreshTokenService, tokenManager);
    }

    @Test
    void given_optionalClient_when_save_should_saveClient() {
        // given
        ApiAccessKcClient client = new ApiAccessKcClient();
        Optional<ApiAccessKcClient> optionalClient = Optional.of(client);
        when(repository.save(client)).thenReturn(client);

        // when
        ApiAccessKcClient result = service.save(optionalClient);

        // should
        assertEquals(client, result);
        verify(repository, times(1)).save(client);
    }

    @Test
    void given_emptyOptional_when_save_should_throwModelIsNullException() {
        // given
        Optional<ApiAccessKcClient> optionalClient = Optional.empty();

        // when & should
        assertThrows(ModelIsNullException.class, () -> service.save(optionalClient));
    }

    @Test
    void when_findAll_should_returnAllClients() {
        // given
        List<ApiAccessKcClient> clients = new ArrayList<>();
        when(repository.findAll()).thenReturn(clients);

        // when
        List<ApiAccessKcClient> result = service.findAll();

        // should
        assertEquals(clients, result);
    }

    @Test
    void given_validId_when_findById_should_returnClient() {
        // given
        String clientId = "client-id";
        ApiAccessKcClient client = new ApiAccessKcClient();
        when(repository.findById(clientId)).thenReturn(Optional.of(client));

        // when
        ApiAccessKcClient result = service.findById(clientId);

        // should
        assertEquals(client, result);
    }

    @Test
    void given_invalidId_when_findById_should_throwModelNotFoundException() {
        // given
        String clientId = "client-id";
        when(repository.findById(clientId)).thenReturn(Optional.empty());

        // when & should
        assertThrows(ModelNotFoundException.class, () -> service.findById(clientId));
    }

    @Test
    void given_optionalClient_when_update_should_updateClient() {
        // given
        ApiAccessKcClient client = new ApiAccessKcClient();
        Optional<ApiAccessKcClient> optionalClient = Optional.of(client);
        when(repository.save(client)).thenReturn(client);

        // when
        ApiAccessKcClient result = service.update(optionalClient);

        // should
        assertEquals(client, result);
        verify(repository, times(1)).save(client);
    }

    @Test
    void given_emptyOptional_when_update_should_throwModelIsNullException() {
        // given
        Optional<ApiAccessKcClient> optionalClient = Optional.empty();

        // when & should
        assertThrows(ModelIsNullException.class, () -> service.update(optionalClient));
    }

    @Test
    void given_validId_when_deleteById_should_deleteClient() {
        // given
        String clientId = "client-id";

        // when
        service.deleteById(clientId);

        // should
        verify(repository, times(1)).deleteById(clientId);
    }

    @Test
    void given_validClientIdAndTokenEndpointUrl_when_generateAccessToken_should_returnAccessToken() {
        // given
        String clientId = "client-id";
        String tokenEndpointUrl = "token-endpoint-url";
        ApiAccessKcClient client = new ApiAccessKcClient(clientId);
        client.setRefreshTokens(new ArrayList<>());
        RefreshToken refreshToken = new RefreshToken();
        AccessToken accessToken = new AccessToken();

        when(repository.save(client)).thenReturn(client);
        when(repository.findById(clientId)).thenReturn(Optional.of(client));
        when(refreshTokenService.generateAndSave(any(), eq(tokenEndpointUrl), eq(tokenManager))).thenReturn(refreshToken);
        when(tokenManager.getAccessToken(client, tokenEndpointUrl, refreshToken)).thenReturn(accessToken);

        // when
        AccessToken result = service.generateAccessToken(clientId, tokenEndpointUrl);

        // should
        assertEquals(accessToken, result);
    }

    @Test
    void given_validClientIdAndTokenEndpointUrl_when_getRefreshToken_should_returnRefreshToken() {
        // given
        String clientId = "client-id";
        String tokenEndpointUrl = "token-endpoint-url";
        ApiAccessKcClient client = new ApiAccessKcClient(clientId); // Set the client ID
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setCreationDate(new Date());
        refreshToken.setExpiresIn(3600);

        client.setRefreshTokens(List.of(refreshToken));
        when(repository.findById(clientId)).thenReturn(Optional.of(client));

        // when
        RefreshToken result = service.getRefreshToken(clientId, tokenEndpointUrl);

        // should
        assertEquals(refreshToken, result);
    }

    @Test
    void given_noRefreshToken_when_getRefreshToken_should_generateAndSaveRefreshToken() {
        // given
        String clientId = "client-id";
        String tokenEndpointUrl = "token-endpoint-url";
        ApiAccessKcClient client = new ApiAccessKcClient(clientId);
        client.setRefreshTokens(new ArrayList<>());
        RefreshToken refreshToken = new RefreshToken();

        when(repository.save(client)).thenReturn(client);
        when(repository.findById(clientId)).thenReturn(Optional.of(client));
        when(refreshTokenService.generateAndSave(any(), eq(tokenEndpointUrl), eq(tokenManager))).thenReturn(refreshToken);

        // when
        RefreshToken result = service.getRefreshToken(clientId, tokenEndpointUrl);

        // should
        assertEquals(refreshToken, result);
        verify(refreshTokenService, times(1)).generateAndSave(any(), eq(tokenEndpointUrl), eq(tokenManager));
    }

    @Test
    void given_expiredRefreshToken_when_getRefreshToken_should_generateAndSaveNewRefreshToken() {
        // given
        String clientId = "client-id";
        String tokenEndpointUrl = "token-endpoint-url";
        ApiAccessKcClient client = new ApiAccessKcClient(clientId);
        client.setRefreshTokens(new ArrayList<>()); // Ensure refresh tokens list is mutable
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setCreationDate(Date.from(Instant.now().minusSeconds(7200)));
        refreshToken.setExpiresIn(3600);

        client.getRefreshTokens().add(refreshToken); // Add refreshToken to the mutable list
        when(repository.save(client)).thenReturn(client);
        when(repository.findById(clientId)).thenReturn(Optional.of(client));
        RefreshToken newRefreshToken = new RefreshToken();
        when(refreshTokenService.generateAndSave(any(), eq(tokenEndpointUrl), eq(tokenManager))).thenReturn(newRefreshToken);

        // when
        RefreshToken result = service.getRefreshToken(clientId, tokenEndpointUrl);

        // should
        assertEquals(newRefreshToken, result);
        verify(refreshTokenService, times(1)).generateAndSave(any(), eq(tokenEndpointUrl), eq(tokenManager));
    }

    @Test
    void given_validClientIdAndTokenEndpointUrl_when_generateAndSaveRefreshToken_should_returnRefreshToken() {
        // given
        String clientId = "client-id";
        String tokenEndpointUrl = "token-endpoint-url";
        ApiAccessKcClient client = new ApiAccessKcClient();
        RefreshToken refreshToken = new RefreshToken();

        when(repository.save(client)).thenReturn(client);
        when(repository.findById(clientId)).thenReturn(Optional.of(client));
        when(refreshTokenService.generateAndSave(any(), eq(tokenEndpointUrl), eq(tokenManager))).thenReturn(refreshToken);

        // when
        RefreshToken result = service.generateAndSaveRefreshToken(clientId, tokenEndpointUrl);

        // should
        assertEquals(refreshToken, result);
        verify(repository, times(1)).save(client);
    }

    @Test
    void given_validClientIdWithNoRefreshTokens_when_getLatestRefreshToken_should_throwNoRecordOfRefreshTokenForTheClientException() {
        // given
        String clientId = "client-id";
        ApiAccessKcClient client = new ApiAccessKcClient(clientId);
        client.setRefreshTokens(new ArrayList<>());

        when(repository.findById(clientId)).thenReturn(Optional.of(client));

        // when & should
        assertThrows(NoRecordOfRefreshTokenForTheClientException.class, () -> service.getLatestRefreshToken(clientId));
    }

    @Test
    void given_validClientIdWithRefreshTokens_when_getLatestRefreshToken_should_returnLatestRefreshToken() throws Exception {
        // given
        String clientId = "client-id";
        ApiAccessKcClient client = new ApiAccessKcClient();
        RefreshToken refreshToken = new RefreshToken();
        RefreshToken latestRefreshToken = new RefreshToken();

        client.setRefreshTokens(List.of(refreshToken, latestRefreshToken));
        when(repository.findById(clientId)).thenReturn(Optional.of(client));

        // when
        RefreshToken result = service.getLatestRefreshToken(clientId);

        // should
        assertEquals(latestRefreshToken, result);
    }
}
