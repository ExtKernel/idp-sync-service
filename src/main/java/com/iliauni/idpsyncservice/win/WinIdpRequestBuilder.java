package com.iliauni.idpsyncservice.win;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iliauni.idpsyncservice.exception.ClientHasNoFqdnOrIpAndPortException;
import com.iliauni.idpsyncservice.exception.KcClientHasNoKcFqdnOrIpAndPortException;
import com.iliauni.idpsyncservice.exception.KcClientHasNoKcRealmException;
import com.iliauni.idpsyncservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.idpsyncservice.idp.IdpRequestBuilder;
import com.iliauni.idpsyncservice.model.WinClient;
import com.iliauni.idpsyncservice.service.Oauth2ClientService;
import com.iliauni.idpsyncservice.service.WinClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WinIdpRequestBuilder implements IdpRequestBuilder<WinClient> {
    private final Oauth2ClientService<WinClient> clientService;
    private final RestTemplateBuilder restTemplateBuilder;

    @Autowired
    public WinIdpRequestBuilder(
            WinClientService clientService,
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
            WinClient client,
            String protocol,
            String endpoint
    ) {
        String host;

        if (client.getFqdn() != null) {
            host = getWinHostUrl(client);
        } else if (client.getIp() != null) {
            host = getWinHostUrl(client);
        } else {
            throw new ClientHasNoFqdnOrIpAndPortException("Client with ID " + client.getId() + " has no FQDN or IP");
        }

        return protocol + "://" + host + endpoint;
    }

    /**
     * @throws KcClientHasNoKcRealmException if the client has no info
     *                                       to get a Keycloak Realm from.
     */
    @Override
    public String buildAuthRequestUrl(
            WinClient client,
            String protocol
    ) {
        String kcFqdn = client.getKcFqdn();
        String kcIp = client.getKcIp();
        String kcPort = client.getKcPort();
        String kcRealm = client.getRealm();

        if (kcRealm == null) {
            throw new KcClientHasNoKcRealmException(
                    "Keycloak client with id "
                            + client.getId()
                            + " has no Keycloak Realm");
        }

        if (kcFqdn != null) {
            return protocol + "://" + kcFqdn + ":" + kcPort
                    + "/realms/" + kcRealm + "/protocol/openid-connect/token";
        } else if (kcIp != null) {
            return protocol + "://" + kcIp + ":" + kcPort
                    + "/realms/" + kcRealm + "/protocol/openid-connect/token";
        } else {
            throw new KcClientHasNoKcFqdnOrIpAndPortException(
                    "Keycloak client with id "
                            + client.getId()
                            + " has no Keycloak FQDN or IP"
            );
        }
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

    private String getWinHostUrl(WinClient client) {
        if (client.getFqdn() != null) {
            return getWinHostname(client);
        } else if (client.getIp() != null) {
            return getWinIpWithPort(client);
        } else {
            throw new ClientHasNoFqdnOrIpAndPortException(
                    "Windows client with ID "
                            + client.getId()
                            + " has no FQDN or IP"
            );
        }
    }

    private String getWinHostname(WinClient client) {
        // get a string until the first dot, which is, basically, a hostname
        return client.getFqdn().split("\\.")[0];
    }

    private String getWinIpWithPort(WinClient client) {
        return client.getIp() + ":" + client.getPort();
    }

    private ObjectNode buildEmptyRequestBody() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.createObjectNode();
    }
}
