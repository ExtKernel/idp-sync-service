package com.iliauni.usersyncglobalservice.cookiejar;

import com.iliauni.usersyncglobalservice.model.IpaClient;
import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;

public interface CookieJarRequestBuilder<T extends IpaClient> {
    public HttpEntity<MultiValueMap<String, String>> buildHttpRequestEntity(T client);
}
