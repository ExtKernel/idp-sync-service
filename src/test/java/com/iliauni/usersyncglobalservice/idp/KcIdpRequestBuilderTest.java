package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.model.AccessToken;
import com.iliauni.usersyncglobalservice.model.KcClient;
import com.iliauni.usersyncglobalservice.service.KcClientService;
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
public class KcIdpRequestBuilderTest {
    @Mock
    KcClientService<KcClient> kcClientService;

    @InjectMocks
    KcIdpRequestBuilder<KcClient> kcIdpRequestBuilder;

    @Test
    public void buildHttpRequestEntityTest_WhenGivenClientIdAndRequestBody_ShouldReturnHttpEntity()
            throws Exception {
        String clientId = "test-client-id";
        AccessToken accessToken = new AccessToken();
        accessToken.setToken("test-access-token");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();

        when(kcClientService.generateAccessToken(clientId)).thenReturn(accessToken);

        assertEquals(new HttpEntity<>(requestBody, headers), kcIdpRequestBuilder.buildHttpRequestEntity(clientId, requestBody));
    }

    @Test
    public void buildApiBaseUrl_WhenGivenKcBaseUrlAndKcRealm_ShouldReturnApiBaseUrl()
            throws Exception{
        String kcBaseUrl = "test-base-url";
        String kcRealm = "test-realm";

        assertEquals("http://" + kcBaseUrl + "/admin/realms/" + kcRealm,
                kcIdpRequestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm));
    }
}
