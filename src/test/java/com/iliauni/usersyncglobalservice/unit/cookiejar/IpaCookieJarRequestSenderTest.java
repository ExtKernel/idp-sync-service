package com.iliauni.usersyncglobalservice.unit.cookiejar;

import com.iliauni.usersyncglobalservice.cookiejar.CookieJarRequestBuilder;
import com.iliauni.usersyncglobalservice.cookiejar.IpaCookieJarRequestSender;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IpaCookieJarRequestSenderTest {
    @Mock
    private CookieJarRequestBuilder<IpaClient> cookieJarRequestBuilder;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private IpaCookieJarRequestSender<IpaClient> ipaCookieJarRequestSender;

    @Test
    public void getCookie_WhenGivenClient_ShouldReturnString()
        throws Exception {
        IpaClient client = new IpaClient();
        client.setId("test-id");

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        requestBody.add("user", client.getPrincipalUsername());
        requestBody.add("password", client.getPrincipalPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.TEXT_PLAIN));
        headers.set("referer", "https://" + null + "/ipa");

        HttpHeaders responseHeaders = new HttpHeaders();
        headers.set("Cookie", "test-cookie");
        ResponseEntity<Map> response = new ResponseEntity<>(responseHeaders, HttpStatus.OK);

        String ipaApiUrl = "test-url";

        when(cookieJarRequestBuilder.buildHttpRequestEntity(client)).thenReturn(new HttpEntity<>(requestBody, headers));
        when(cookieJarRequestBuilder.getRestTemplate()).thenReturn(restTemplate);
        when(cookieJarRequestBuilder.buildApiBaseUrl(any(), any())).thenReturn(ipaApiUrl);
        when(restTemplate.exchange(
                ipaApiUrl,
                HttpMethod.POST,
                new HttpEntity<>(requestBody, headers),
                Map.class)).thenReturn(response);

        assertEquals(response.getHeaders().getFirst(HttpHeaders.SET_COOKIE), ipaCookieJarRequestSender.getCookie(client));
    }
}
