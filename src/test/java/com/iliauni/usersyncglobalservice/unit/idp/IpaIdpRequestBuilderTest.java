package com.iliauni.usersyncglobalservice.unit.idp;

import com.iliauni.usersyncglobalservice.idp.IpaIdpRequestBuilder;
import com.iliauni.usersyncglobalservice.model.CookieJar;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.service.IpaClientService;
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
public class IpaIdpRequestBuilderTest {
    @Mock
    IpaClientService<IpaClient> ipaClientService;

    @InjectMocks
    IpaIdpRequestBuilder<IpaClient> ipaIdpRequestBuilder;

    @Test
    public void buildHttpRequestEntityTest_WhenGivenClientIdAndRequestBody_ShouldReturnHttpEntity()
            throws Exception {
        String clientId = "test-client-id";
        CookieJar cookieJar = new CookieJar();
        cookieJar.setCookie("test-cookie");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", cookieJar.getCookie());
        headers.add("Referer", "https://" + null + "/ipa");
        headers.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();

        when(ipaClientService.generateAndSaveCookieJar(clientId)).thenReturn(cookieJar);

        assertEquals(new HttpEntity<>(requestBody, headers), ipaIdpRequestBuilder.buildHttpRequestEntity(clientId, requestBody));
    }

    @Test
    public void buildApiBaseUrl_WhenGivenKcBaseUrlAndKcRealm_ShouldReturnApiBaseUrl()
            throws Exception{
        String ipaBaseUrl = "test-base-url";
        String ipaApiEndpoint = "test-api-endpoint";

        assertEquals("https://" + ipaBaseUrl + ipaApiEndpoint,
                ipaIdpRequestBuilder.buildApiBaseUrl(ipaBaseUrl, ipaApiEndpoint));
    }
}
