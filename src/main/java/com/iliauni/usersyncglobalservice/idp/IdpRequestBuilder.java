package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.model.Client;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

/**
 * An interface for building HTTP request entities and API base URLs in an Identity Provider (IDP) context.
 */
public interface IdpRequestBuilder<T extends Client> {
    /**
     * Builds an HTTP request entity for the IDP API.
     *
     * @param clientId the client ID to use for the request
     * @param requestBody the request body to include in the entity
     * @param authEndpointUrl a full url of the endpoint from which access token should be retrieved
     * @return the HTTP request entity
     */
    HttpEntity buildHttpRequestEntity(
            String clientId,
            String requestBody,
            String authEndpointUrl);

    /**
     * Builds an HTTP request entity for the IDP API.
     * The entity will include only headers to authenticate/authorize
     *
     * @param clientId the client ID to use for the request
     * @param authEndpointUrl a full url of the endpoint from which access token should be retrieved
     * @return the HTTP request entity
     */
    HttpEntity buildAuthOnlyHttpRequestEntity(
            String clientId,
            String authEndpointUrl
    );

    /**
     * Builds the base URL for the IDP API request.
     *
     * @param client the client to get the IDP info for auth from
     * @param protocol the protocol to use for the request
     * @param endpoint the specific endpoint of the IDP API
     * @return the full request URL
     */
    String buildRequestUrl(
            T client,
            String protocol,
            String endpoint
    );

    /**
     * Builds a URL for the auth request.
     *
     * @param client the client to get the IDP info for auth from
     * @return the full auth URL
     */
    String buildAuthRequestUrl(
            T client,
            String protocol
    );

    /**
     * Retrieves the RestTemplate instance associated with this request builder.
     *
     * @return the RestTemplate instance
     */
    RestTemplate getRestTemplate();
}
