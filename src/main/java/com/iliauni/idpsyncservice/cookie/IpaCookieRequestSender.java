package com.iliauni.idpsyncservice.cookie;

import com.iliauni.idpsyncservice.exception.ClientHasNoFqdnOrIpAndPortException;
import com.iliauni.idpsyncservice.model.IpaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * A component class implementing the {@link CookieRequestSender} interface for sending requests to obtain cookies from the FreeIPA IDP system.
 *
 * @param <T> the type of FreeIPA client used for the request
 */
@Component
public class IpaCookieRequestSender<T extends IpaClient> implements CookieRequestSender<T> {
    private final CookieRequestBuilder<T> requestBuilder;

    /**
     * Constructs an {@code IpaCookieJarRequestSender} instance with the specified {@link CookieRequestBuilder}.
     *
     * @param requestBuilder the request builder for obtaining cookies
     */
    @Autowired
    public IpaCookieRequestSender(
            CookieRequestBuilder<T> requestBuilder
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
        ResponseEntity<Map> response = requestBuilder.getRestTemplate(client).exchange(
                "https://" + getClientHostUrl(client) + endpointUrl,
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(client),
                Map.class);

        return response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
    }

    private String getClientHostUrl(IpaClient client) {
        String clientFqdn = client.getFqdn();

        if (clientFqdn != null) {
            return clientFqdn;
        } else {
            throw new ClientHasNoFqdnOrIpAndPortException(
                    "FreeIPA client with ID "
                            + client.getId()
                            + " has no FQDN"
            );
        }
    }
}
