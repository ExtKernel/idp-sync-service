package com.iliauni.usersyncglobalservice.unit.idp.win;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.iliauni.usersyncglobalservice.idp.win.WinIdpRequestBuilder;
import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.service.WinClientService;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@ExtendWith(MockitoExtension.class)
public class WinIdpRequestBuilderTest {
    @Mock
    private WinClientService winClientService;

    @InjectMocks
    private WinIdpRequestBuilder winIdpRequestBuilder;

    @Test
    public void buildHttpRequestEntityTest_WhenGivenClientIdAndRequestBody_ShouldReturnHttpEntity()
            throws Exception {
        String clientId = "test-client-id";
        AccessToken accessToken = new AccessToken();
        accessToken.setToken("test-access-token");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();

        when(winClientService.generateAccessToken(any(), any())).thenReturn(accessToken);

        assertEquals(new HttpEntity<>(requestBody, headers), winIdpRequestBuilder.buildHttpRequestEntity(
                clientId,
                requestBody,
                null
        ));
    }

    @Test
    public void buildApiBaseUrl_WhenGivenKcBaseUrlAndKcRealm_ShouldReturnApiBaseUrl()
            throws Exception{
        String hostname = "test-hostname";
        String endpoint = "/test-realm/";

        assertEquals(
                "http://" + hostname + endpoint,
                winIdpRequestBuilder.buildApiBaseUrl(hostname, endpoint)
        );
    }
}
