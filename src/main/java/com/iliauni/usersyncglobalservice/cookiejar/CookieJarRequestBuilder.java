package com.iliauni.usersyncglobalservice.cookiejar;

import com.iliauni.usersyncglobalservice.model.IpaClient;
import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public interface CookieJarRequestBuilder<T extends IpaClient> {
    HttpEntity<MultiValueMap<String, String>> buildHttpRequestEntity(T client);
    String buildApiBaseUrl(String baseUrl, String endpoint);
    RestTemplate getRestTemplate();
}
