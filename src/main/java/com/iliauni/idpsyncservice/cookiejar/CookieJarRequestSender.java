package com.iliauni.idpsyncservice.cookiejar;

import com.iliauni.idpsyncservice.model.IpaClient;

/**
 * An interface for sending requests to an Identity Provider (IDP), which use cookie for auth.
 *
 * @param <T> the type of client used for the request
 */
public interface CookieJarRequestSender<T extends IpaClient> {

    /**
     * Retrieves a cookie token for the specified client.
     *
     * @param client the client for which the cookie token is requested
     * @return the cookie token obtained from the IDP
     */
    String getCookie(
            T client,
            String endpointUrl
    );
}
