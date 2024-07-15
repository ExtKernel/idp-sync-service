package com.iliauni.idpsyncservice.cookiejar;

import com.iliauni.idpsyncservice.model.IpaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * A component class implementing the {@link CookieJarRequestSender} interface for sending requests to obtain cookies from the FreeIPA IDP system.
 *
 * @param <T> the type of FreeIPA client used for the request
 */
@Component
public class IpaCookieJarRequestSender<T extends IpaClient> implements CookieJarRequestSender<T> {
    private final CookieJarRequestBuilder<T> requestBuilder;

    /**
     * Constructs an {@code IpaCookieJarRequestSender} instance with the specified {@link CookieJarRequestBuilder}.
     *
     * @param requestBuilder the request builder for obtaining cookies
     */
    @Autowired
    public IpaCookieJarRequestSender(
            CookieJarRequestBuilder<T> requestBuilder
    ) {
        this.requestBuilder = requestBuilder;
    }

    /**
     * @inheritDoc
     * Generates and retrieves a cookie from the FreeIPA IDP system.
     */
    @Override
    public String getCookie(
            T client,
            String endpointUrl
    ) {
        ResponseEntity<Map> response = requestBuilder.getRestTemplate().exchange(
                endpointUrl,
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(client),
                Map.class);

        return response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
    }
}
