package com.iliauni.idpsyncservice.cookie;

import com.iliauni.idpsyncservice.model.IpaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A component class implementing the {@link CookieRetriever} interface for retrieving cookies from the FreeIPA IDP system.
 *
 * @param <T> the type of FreeIPA client used for the retrieval
 */
@Component
public class IpaCookieRetriever<T extends IpaClient> implements CookieRetriever<T> {
    CookieRequestSender<T> requestSender;

    /**
     * Constructs an {@code IpaCookieJarRetriever} instance with the specified {@link CookieRequestSender}.
     *
     * @param requestSender the request sender for retrieving cookies
     */
    @Autowired
    public IpaCookieRetriever(
            CookieRequestSender<T> requestSender
    ) {
        this.requestSender = requestSender;
    }

    /**
     * @inheritDoc
     * Retrieves a cookie from the FreeIPA IDP system.
     */
    @Override
    public String retrieveCookieJar(
            T client,
            String endpointUrl
    ) {
        return requestSender.getCookie(client, endpointUrl);
    }
}
