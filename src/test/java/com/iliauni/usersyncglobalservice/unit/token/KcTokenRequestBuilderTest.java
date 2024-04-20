package com.iliauni.usersyncglobalservice.unit.token;

import com.iliauni.usersyncglobalservice.model.KcClient;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import com.iliauni.usersyncglobalservice.service.KcClientService;
import com.iliauni.usersyncglobalservice.token.KcTokenRequestBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KcTokenRequestBuilderTest {
    @Mock
    private KcClientService kcClientService;

    @InjectMocks
    private KcTokenRequestBuilder kcTokenRequestBuilder;

    @Test
    public void buildHttpRequestEntity_WhenGivenPasswordGrantTypeAndClient_ShouldReturnHttpEntity()
        throws Exception {
        KcClient client = buildClientObject();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", client.getId());
        requestBody.add("client_secret", client.getClientSecret());
        requestBody.add("username", client.getPrincipalUsername());
        requestBody.add("password", client.getPrincipalPassword());
        requestBody.add("grant_type", "password");
        requestBody.add("scope", "openid");

        assertEquals(new HttpEntity<>(requestBody, buildHeaders()), kcTokenRequestBuilder.buildHttpRequestEntityWithPasswordGrantType(client));
    }

    @Test
    public void buildHttpRequestEntity_WhenGivenRefreshTokenGrantTypeAndClient_ShouldReturnHttpEntity()
        throws Exception {
        KcClient client = buildClientObject();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("test-refresh-token");

        when(kcClientService.getLatestRefreshToken(client.getId())).thenReturn(refreshToken);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", client.getId());
        requestBody.add("client_secret", client.getClientSecret());
        requestBody.add("refresh_token", refreshToken.getToken());
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("scope", "openid");

        assertEquals(new HttpEntity<>(requestBody, buildHeaders()), kcTokenRequestBuilder.buildHttpRequestEntityWithRefreshTokenGrantType(client, refreshToken.getToken()));
    }

    private KcClient buildClientObject() {
        KcClient client = new KcClient();
        client.setId("test-id");
        client.setClientSecret("test-secret");
        client.setPrincipalUsername("test-username");
        client.setPrincipalPassword("test-password");

        return client;
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return headers;
    }
}
