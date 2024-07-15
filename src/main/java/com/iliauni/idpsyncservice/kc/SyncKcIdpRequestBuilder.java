package com.iliauni.idpsyncservice.kc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iliauni.idpsyncservice.exception.ClientHasNoFqdnOrIpAndPortException;
import com.iliauni.idpsyncservice.exception.KcClientHasNoKcFqdnOrIpAndPortException;
import com.iliauni.idpsyncservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.idpsyncservice.idp.IdpRequestBuilder;
import com.iliauni.idpsyncservice.model.ApiAccessKcClient;
import com.iliauni.idpsyncservice.model.SyncKcClient;
import com.iliauni.idpsyncservice.service.Oauth2ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * A component class implementing the {@link IdpRequestBuilder} interface
 * for building HTTP request entities and API base URLs specific to Keycloak (KC) IDP systems.
 */
@Component
public class SyncKcIdpRequestBuilder implements IdpRequestBuilder<SyncKcClient> {
    private final Oauth2ClientService<ApiAccessKcClient> clientService;
    private final RestTemplateBuilder restTemplateBuilder;

    @Autowired
    public SyncKcIdpRequestBuilder(
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
            SyncKcClient client,
            String protocol,
            String endpoint
    ) {
        return protocol + "://"
                + getClientHostUrl(client)
                + "/admin/realms/"
                + client.getRealm()
                + endpoint;
    }

    @Override
    public String buildAuthRequestUrl(
            SyncKcClient client,
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
     * @param accessToken the access token to include in the headers.
     * @return the constructed HTTP headers.
     */
    private HttpHeaders buildHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    /**
     * @param client a client to get a host url from.
     * @return the host url of the client.
     * @throws ClientHasNoFqdnOrIpAndPortException if the client has no info
     *                                             to get and build a host url from.
     */
    private String getClientHostUrl(SyncKcClient client) {
        String clientFqdn = client.getFqdn();
        String clientIp = client.getIp();
        String clientPort = client.getPort();

        if (clientFqdn != null) {
            return clientFqdn;
        } else if (clientIp != null & clientPort != null) {
            return mergeHostWithPort(clientIp, clientPort);
        } else {
            throw new ClientHasNoFqdnOrIpAndPortException(
                    "Keycloak client with ID "
                            + client.getId()
                            + " has no FQDN, IP or port"
            );
        }
    }

    /**
     * @param client a client to get a Keycloak host url from.
     * @return the host url of the client's Keycloak server.
     * @throws KcClientHasNoKcFqdnOrIpAndPortException if the client has no info
     *                                                 to get and build a Keycloak host url from.
     */
    private String getClientKcHostUrl(SyncKcClient client) {
        String kcFqdn = client.getKcFqdn();
        String kcIp = client.getKcIp();
        String kcPort = client.getPort();

        if (kcFqdn != null) {
            return kcFqdn;
        } else if (kcIp != null & kcPort != null) {
            return mergeHostWithPort(kcIp, kcPort);
        } else {
            throw new KcClientHasNoKcFqdnOrIpAndPortException(
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
