package com.iliauni.usersyncglobalservice.unit.cookiejar;

import com.iliauni.usersyncglobalservice.cookiejar.IpaCookieJarRequestBuilder;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IpaCookieJarRequestBuilderTest {

    @Test
    public void buildHttpRequestEntity_WhenGivenClient_ShouldReturnHttpEntity()
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

        IpaCookieJarRequestBuilder<IpaClient> ipaClientIpaCookieJarRequestBuilder = new IpaCookieJarRequestBuilder<>();
        assertEquals(new HttpEntity<>(requestBody, headers), ipaClientIpaCookieJarRequestBuilder.buildHttpRequestEntity(client));
    }
}
