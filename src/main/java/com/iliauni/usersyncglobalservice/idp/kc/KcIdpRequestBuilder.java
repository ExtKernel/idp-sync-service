package com.iliauni.usersyncglobalservice.idp.kc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iliauni.usersyncglobalservice.exception.ClientHasNoFqdnOrIpOrPortException;
import com.iliauni.usersyncglobalservice.exception.KcClientHasNoKcFqdnOrIpOrPortException;
import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.idp.IdpRequestBuilder;
import com.iliauni.usersyncglobalservice.model.ApiAccessKcClient;
import com.iliauni.usersyncglobalservice.model.KcClient;
import com.iliauni.usersyncglobalservice.service.SyncKcClientService;
import com.iliauni.usersyncglobalservice.service.Oauth2ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * A component class implementing the {@link IdpRequestBuilder} interface for building HTTP request entities and API base URLs specific to Keycloak (KC) IDP systems.
 */
@Component
public class KcIdpRequestBuilder implements IdpRequestBuilder<KcClient> {
    private final Oauth2ClientService<ApiAccessKcClient> clientService;
    private final RestTemplateBuilder restTemplateBuilder;

    /**
     * Constructs a {@code KcIdpRequestBuilder} instance with the specified {@link SyncKcClientService}.
     *
     * @param clientService the service for Admin CLI clients
     */
    @Autowired
    public KcIdpRequestBuilder(
            Oauth2ClientService<ApiAccessKcClient> clientService,
            RestTemplateBuilder restTemplateBuilder
    ) {
        this.clientService = clientService;
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Override
    public HttpEntity<String> buildHttpRequestEntity(
            String clientId,
            String requestBody,
            String tokenEndpointUrl
    ) {
        return new HttpEntity<>(
                requestBody,
                buildHeaders(
                        clientService.generateAccessToken(
                                clientId,
                                tokenEndpointUrl
                        ).getToken()
                )
        );
    }

    @Override
    public HttpEntity buildAuthOnlyHttpRequestEntity(
            String clientId,
            String tokenEndpointUrl
    ) {
        return new HttpEntity(
                buildEmptyRequestBody(),
                buildHeaders(
                        clientService.generateAccessToken(
                                clientId,
                                tokenEndpointUrl
                        ).getToken()
                )
        );
    }

    @Override
    public String buildRequestUrl(
            KcClient client,
            String protocol,
            String endpoint
    ) {
        return protocol + "://" + getClientHostUrl(client) + endpoint;
    }

    @Override
    public String buildAuthRequestUrl(
            KcClient client,
            String protocol
    ) {
        return protocol + "://"
                + getClientKcHostUrl(client)
                + "/realms/master/protocol/openid-connect/token";
    }

    @Override
    public RestTemplate getRestTemplate() {
        return restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
    }

    /**
     * Builds the HTTP headers for the request with the given access token.
     *
     * @param accessToken the access token to include in the headers
     * @return the constructed HTTP headers
     */
    private HttpHeaders buildHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    private String getClientHostUrl(KcClient client) {
        String clientFqdn = client.getFqdn();
        String clientIp = client.getIp();
        String clientPort = client.getPort();

        if (clientFqdn != null & clientPort != null) {
            return mergeHostWithPort(clientFqdn, clientPort);
        } else if (clientIp != null & clientPort != null) {
            return mergeHostWithPort(clientIp, clientPort);
        } else {
            throw new ClientHasNoFqdnOrIpOrPortException(
                    "Keycloak client with ID "
                            + client.getId()
                            + " has no FQDN, IP or port"
            );
        }
    }

    private String getClientKcHostUrl(KcClient client) {
        String kcFqdn = client.getKcFqdn();
        String kcIp = client.getKcIp();
        String kcPort = client.getPort();

        if (kcFqdn != null & kcPort != null) {
            return mergeHostWithPort(kcFqdn, kcPort);
        } else if (kcIp != null & kcPort != null) {
            return mergeHostWithPort(kcIp, kcPort);
        } else {
            throw new KcClientHasNoKcFqdnOrIpOrPortException(
                    "Keycloak client with id "
                            + client.getId()
                            + " has no Keycloak FQDN, IP or port"
            );
        }
    }

    private String mergeHostWithPort(
            String host,
            String port
    ) {
        return host + ":" + port;
    }

    private ObjectNode buildEmptyRequestBody() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.createObjectNode();
    }
}
