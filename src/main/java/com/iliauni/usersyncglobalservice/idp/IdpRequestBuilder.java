package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.model.Client;
import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;

public interface IdpRequestBuilder<T extends Client> {
    HttpEntity<MultiValueMap<String, Object>> buildHttpRequestEntity(String clientId, MultiValueMap<String, Object> requestBody);
    String buildApiBaseUrl(String baseUrl, String endpoint);
}
