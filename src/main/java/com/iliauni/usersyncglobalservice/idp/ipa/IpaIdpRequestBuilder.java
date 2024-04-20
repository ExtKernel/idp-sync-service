package com.iliauni.usersyncglobalservice.idp.ipa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iliauni.usersyncglobalservice.idp.IdpRequestBuilder;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.service.CookieClientService;
import com.iliauni.usersyncglobalservice.service.IpaClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * A component class implementing the {@link IdpRequestBuilder} interface for building HTTP request entities and API base URLs specific to FreeIPA (Identity, Policy, and Audit) IDP systems.
 */
@Component
public class IpaIdpRequestBuilder implements IdpRequestBuilder {
    CookieClientService<IpaClient> clientService;

    /**
     * Constructs an {@code IpaIdpRequestBuilder} instance with the specified {@link IpaClientService}.
     *
     * @param cookieClientService the service for IPA clients
     */
    @Autowired
    public IpaIdpRequestBuilder(CookieClientService<IpaClient> cookieClientService) {
        this.clientService = cookieClientService;
    }

    /**
     * @inheritDoc
     * Generates and includes a cookie in the request headers using the IPA client service.
     */
    @Override
    public HttpEntity<Map<String, Object>> buildHttpRequestEntity(
            String clientId,
            Map<String, Object> requestBody,
            String authEndpointUrl
    ) {
        return new HttpEntity<>(
                requestBody,
                buildHeaders(
                        clientService.generateAndSaveCookieJar(
                                clientId,
                                authEndpointUrl
                        ).getCookie(),
                        getIpaHostname(clientService.findById(clientId))
                )
        );
    }

    @Override
    public HttpEntity buildOnlyAuthHttpRequestEntity(
            String clientId,
            String authEndpointUrl
    ) {
        return new HttpEntity(
                buildEmptyRequestBody(),
                buildHeaders(
                        clientService.generateAndSaveCookieJar(
                                clientId,
                                authEndpointUrl
                        ).getCookie(),
                        getIpaHostname(clientService.findById(clientId))
                )
        );
    }

    @Override
    public String buildApiBaseUrl(
            String hostname,
            String endpoint
    ) {
        return "https://" + hostname + endpoint;
    }

    /**
     * Builds the HTTP headers for the request with the given cookie.
     *
     * @param cookie the cookie to include in the headers
     * @return the constructed HTTP headers
     */
    private HttpHeaders buildHeaders(
            String cookie,
            String hostname
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        headers.add("Referer", "https://" + hostname + "/ipa");
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    private String getIpaHostname(IpaClient client) {
        // get a string until the first dot, which is, basically, a hostname
        return client.getFqdn().split("\\.")[0];
    }

    private ObjectNode buildEmptyRequestBody() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.createObjectNode();
    }
}
