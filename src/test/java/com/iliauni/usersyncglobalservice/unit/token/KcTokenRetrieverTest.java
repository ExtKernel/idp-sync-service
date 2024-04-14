package com.iliauni.usersyncglobalservice.unit.token;

import com.iliauni.usersyncglobalservice.exception.NoRecordOfRefreshTokenForTheClientException;
import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.KcClient;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.service.KcClientService;
import com.iliauni.usersyncglobalservice.token.KcTokenRetriever;
import com.iliauni.usersyncglobalservice.token.TokenRequestSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KcTokenRetrieverTest {
    @Mock
    private KcClientService<KcClient> kcClientService;

    @Mock
    private TokenRequestSender<KcClient> kcTokenRequestSender;

    @InjectMocks
    private KcTokenRetriever<KcClient> kcTokenRetriever;

    private final String tokenEndpointUrl = "test-token-endpoint-url";

    @Test
    public void retrieveAccessToken_WhenGivenClientAndTokenEndpointUrlAndRefreshToken_ShouldReturnAccessToken()
        throws Exception {
        KcClient client = buildClientObject();
        AccessToken accessToken = buildAccessTokenObject();
        RefreshToken refreshToken = buildRefreshTokenObject();

        when(kcClientService.getLatestRefreshToken(client.getId())).thenReturn(refreshToken);
        when(kcTokenRequestSender.getAccessTokenByRefreshToken(client, tokenEndpointUrl)).thenReturn(accessToken);

        assertEquals(accessToken.getToken(), kcTokenRetriever.retrieveAccessToken(client, tokenEndpointUrl).getToken());
        assertEquals(accessToken.getExpiresIn(), kcTokenRetriever.retrieveAccessToken(client, tokenEndpointUrl).getExpiresIn());
    }

    @Test
    public void retrieveAccessToken_WhenGivenClientAndTokenEndpointUrlAndNoRefreshToken_ShouldReturnAccessToken()
        throws Exception {
        KcClient client = buildClientObject();
        AccessToken accessToken = buildAccessTokenObject();
        RefreshToken refreshToken = buildRefreshTokenObject();

        when(kcClientService.getLatestRefreshToken(client.getId())).thenThrow(new NoRecordOfRefreshTokenForTheClientException("test-no-record-of-refresh-token-exception"));
        when(kcClientService.generateAndSaveRefreshToken(client.getId())).thenReturn(refreshToken);
        when(kcTokenRequestSender.getAccessTokenByRefreshToken(client, tokenEndpointUrl)).thenReturn(accessToken);

        assertEquals(accessToken.getToken(), kcTokenRetriever.retrieveAccessToken(client, tokenEndpointUrl).getToken());
        assertEquals(accessToken.getExpiresIn(), kcTokenRetriever.retrieveAccessToken(client, tokenEndpointUrl).getExpiresIn());
    }

    @Test
    public void retrieveRefreshToken_WhenGivenClientAndTokenEndpointUrl_ShouldReturnRefreshToken()
        throws Exception {
        KcClient client = buildClientObject();
        RefreshToken refreshToken = buildRefreshTokenObject();

        when(kcTokenRequestSender.getRefreshToken(client, tokenEndpointUrl)).thenReturn(refreshToken);

        assertEquals(refreshToken.getToken(), kcTokenRetriever.retrieveRefreshToken(client, tokenEndpointUrl).getToken());
        assertEquals(refreshToken.getExpiresIn(), kcTokenRetriever.retrieveRefreshToken(client, tokenEndpointUrl).getExpiresIn());
    }

    private KcClient buildClientObject() {
        KcClient client = new KcClient();
        client.setId("test-id");
        client.setClientSecret("test-secret");
        client.setPrincipalUsername("test-username");
        client.setPrincipalPassword("test-password");

        return client;
    }

    private AccessToken buildAccessTokenObject() {
        AccessToken accessToken = new AccessToken();
        accessToken.setToken("test-access-token");
        accessToken.setExpiresIn(3600);

        return accessToken;
    }

    private RefreshToken buildRefreshTokenObject() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("test-refresh-token");
        refreshToken.setExpiresIn(3600);

        return refreshToken;
    }
}
