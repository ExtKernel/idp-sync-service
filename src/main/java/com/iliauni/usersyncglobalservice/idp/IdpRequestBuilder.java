package com.iliauni.usersyncglobalservice.idp;

import org.springframework.http.HttpEntity;

import java.util.Map;

/**
 * An interface for building HTTP request entities and API base URLs in an Identity Provider (IDP) context.
 */
public interface IdpRequestBuilder {
    /**
     * Builds an HTTP request entity for the IDP API.
     *
     * @param clientId    the client ID to use for the request
     * @param requestBody the request body to include in the entity
     * @param authEndpointUrl a full url of the endpoint from which access token should be retrieved
     * @return the HTTP request entity
     */
    HttpEntity buildHttpRequestEntity(
            String clientId,
            Map<String, Object> requestBody,
            String authEndpointUrl);

    /**
     * Builds an HTTP request entity for the IDP API.
     * The entity will include only headers to authenticate/authorize
     *
     * @param clientId    the client ID to use for the request
     * @param authEndpointUrl a full url of the endpoint from which access token should be retrieved
     * @return the HTTP request entity
     */
    HttpEntity buildOnlyAuthHttpRequestEntity(
            String clientId,
            String authEndpointUrl
    );

    /**
     * Builds the base URL for the IDP API request.
     *
     * @param baseUrl  the base URL of the IDP API
     * @param endpoint the specific endpoint of the IDP API
     * @return the full API base URL
     */
    String buildApiBaseUrl(String baseUrl, String endpoint);
}
