package com.iliauni.idpsyncservice.cookiejar;

import com.iliauni.idpsyncservice.model.IpaClient;
import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * A builder interface for constructing HTTP requests and API base URLs in the context of a Cookie Jar system.
 *
 * @param <T> the type of client used for the request
 */
public interface CookieJarRequestBuilder<T extends IpaClient> {

    /**
     * Builds an HTTP request entity for the given client.
     *
     * @param client the client for which the request entity is built
     * @return the HTTP request entity
     */
    HttpEntity<MultiValueMap<String, String>> buildHttpRequestEntity(T client);

    /**
     * Retrieves the RestTemplate instance associated with this request builder.
     *
     * @return the RestTemplate instance
     */
    RestTemplate getRestTemplate();
}
