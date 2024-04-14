package com.iliauni.usersyncglobalservice.unit.token;

import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.KcClient;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.token.KcTokenRequestBuilder;
import com.iliauni.usersyncglobalservice.token.KcTokenRequestSender;
import com.iliauni.usersyncglobalservice.token.TokenExtractor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KcTokenRequestSenderTest {
    @Mock
    private KcTokenRequestBuilder<KcClient> kcTokenRequestBuilder;

    @Mock
    private TokenExtractor tokenExtractor;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private KcTokenRequestSender<KcClient> kcTokenRequestSender;

    private final String tokenEndpointUrl = "test-token-endpoint-url";

    @Test
    public void getAccessTokenByRefreshToken_WhenGivenClientAndTokenEndpointUrl_ShouldReturnAccessToken()
        throws Exception {
        KcClient client = buildClientObject();
        AccessToken accessToken = buildAccessTokenObject();
        Map<String, Object> jsonMap = buildAccessTokenMap(accessToken);

        when(restTemplate.exchange(
                tokenEndpointUrl,
                HttpMethod.POST,
                kcTokenRequestBuilder.buildHttpRequestEntity(
                        "refresh_token",
                        client
                ),
                Map.class)
        ).thenReturn(new ResponseEntity<>(jsonMap, HttpStatus.OK));
        when(tokenExtractor.extractAccessTokenFromJsonMap(jsonMap)).thenReturn(accessToken);

        assertEquals(accessToken.getToken(), kcTokenRequestSender.getAccessTokenByRefreshToken(client, tokenEndpointUrl).getToken());
        assertEquals(accessToken.getExpiresIn(), kcTokenRequestSender.getAccessTokenByRefreshToken(client, tokenEndpointUrl).getExpiresIn());
    }

    @Test
    public void getAccessTokenByCredentials_WhenGivenClientAndTokenEndpointUrl_ShouldReturnAccessToken()
        throws Exception {
        KcClient client = buildClientObject();
        AccessToken accessToken = buildAccessTokenObject();
        Map<String, Object> jsonMap = buildAccessTokenMap(accessToken);

        when(restTemplate.exchange(
                tokenEndpointUrl,
                HttpMethod.POST,
                kcTokenRequestBuilder.buildHttpRequestEntity(
                        "password",
                        client
                ),
                Map.class)
        ).thenReturn(new ResponseEntity<>(jsonMap, HttpStatus.OK));
        when(tokenExtractor.extractAccessTokenFromJsonMap(jsonMap)).thenReturn(accessToken);

        assertEquals(accessToken.getToken(), kcTokenRequestSender.getAccessTokenByRefreshToken(client, tokenEndpointUrl).getToken());
        assertEquals(accessToken.getExpiresIn(), kcTokenRequestSender.getAccessTokenByRefreshToken(client, tokenEndpointUrl).getExpiresIn());
    }

    @Test
    public void getRefreshToken_WhenGivenClientAndTokenEndpointUrl_ShouldReturnRefreshToken()
        throws Exception {
        KcClient client = buildClientObject();
        RefreshToken refreshToken = buildRefreshTokenObject();
        Map<String, Object> jsonMap = buildRefreshTokenMap(refreshToken);

        when(restTemplate.exchange(
                tokenEndpointUrl,
                HttpMethod.POST,
                kcTokenRequestBuilder.buildHttpRequestEntity(
                        "password",
                        client
                ),
                Map.class)
        ).thenReturn(new ResponseEntity<>(jsonMap, HttpStatus.OK));
        when(tokenExtractor.extractRefreshTokenFromJsonMap(jsonMap)).thenReturn(refreshToken);

        assertEquals(refreshToken.getToken(), kcTokenRequestSender.getRefreshToken(client, tokenEndpointUrl).getToken());
        assertEquals(refreshToken.getExpiresIn(), kcTokenRequestSender.getRefreshToken(client, tokenEndpointUrl).getExpiresIn());
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

    private Map<String, Object> buildAccessTokenMap(AccessToken accessToken) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("access_token", accessToken.getToken());
        jsonMap.put("expires_in", accessToken.getExpiresIn());

        return jsonMap;
    }

    private Map<String, Object> buildRefreshTokenMap(RefreshToken refreshToken) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("refresh_token", refreshToken.getToken());
        jsonMap.put("refresh_expires_in", refreshToken.getExpiresIn());

        return jsonMap;
    }
}
