package com.iliauni.usersyncglobalservice.cookiejar;

/**
 * An interface for retrieving a cookie jar for a given client.
 *
 * @param <T> the type of client for which the cookie jar is retrieved
 */
public interface CookieJarRetriever<T> {

    /**
     * Retrieves the cookie jar for the specified client.
     *
     * @param client the client for which the cookie jar is retrieved
     * @return the cookie jar obtained for the client
     */
    String retrieveCookieJar(
            T client,
            String endpointUrl
    );
}
