package com.iliauni.usersyncglobalservice.unit.idp.ipa;

import com.iliauni.usersyncglobalservice.idp.ipa.IpaIdpRequestBuilder;
import com.iliauni.usersyncglobalservice.model.Cookie;
import com.iliauni.usersyncglobalservice.service.IpaClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IpaIdpRequestBuilderTest {
    @Mock
    private IpaClientService ipaClientService;

    @InjectMocks
    private IpaIdpRequestBuilder ipaIdpRequestBuilder;

    @Test
    public void buildHttpRequestEntityTest_WhenGivenClientIdAndRequestBody_ShouldReturnHttpEntity()
            throws Exception {
        String clientId = "test-client-id";
        Cookie cookie = new Cookie();
        cookie.setCookie("test-cookie");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", cookie.getCookie());
        headers.add("Referer", "https://" + null + "/ipa");
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();

        when(ipaClientService.generateAndSaveCookieJar(clientId, null)).thenReturn(cookie);

        assertEquals(new HttpEntity<>(requestBody, headers), ipaIdpRequestBuilder.buildHttpRequestEntity(
                clientId,
                requestBody,
                null
        ));
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
