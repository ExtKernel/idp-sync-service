package com.iliauni.usersyncglobalservice.cookiejar;

import com.iliauni.usersyncglobalservice.model.IpaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A component class implementing the {@link CookieJarRetriever} interface for retrieving cookies from the FreeIPA IDP system.
 *
 * @param <T> the type of IPA client used for the retrieval
 */
@Component
public class IpaCookieJarRetriever<T extends IpaClient> implements CookieJarRetriever<T> {
    CookieJarRequestSender<T> requestSender;

    /**
     * Constructs an {@code IpaCookieJarRetriever} instance with the specified {@link CookieJarRequestSender}.
     *
     * @param requestSender the request sender for retrieving cookies
     */
    @Autowired
    public IpaCookieJarRetriever(
            CookieJarRequestSender<T> requestSender
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
