package com.iliauni.usersyncglobalservice.idp.kc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iliauni.usersyncglobalservice.idp.IdpRequestBuilder;
import com.iliauni.usersyncglobalservice.model.ApiAccessKcClient;
import com.iliauni.usersyncglobalservice.service.KcClientService;
import com.iliauni.usersyncglobalservice.service.Oauth2ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * A component class implementing the {@link IdpRequestBuilder} interface for building HTTP request entities and API base URLs specific to Keycloak (KC) IDP systems.
 */
@Component
public class KcIdpRequestBuilder implements IdpRequestBuilder {
    private final Oauth2ClientService<ApiAccessKcClient> clientService;

    @Value("${kcAdminCliClientId}")
    private String kcAdminCliClientId;

    /**
     * Constructs a {@code KcIdpRequestBuilder} instance with the specified {@link KcClientService}.
     *
     * @param clientService the service for Admin CLI clients
     */
    @Autowired
    public KcIdpRequestBuilder(
            Oauth2ClientService<ApiAccessKcClient> clientService
    ) {
        this.clientService = clientService;
    }

    /**
     * @inheritDoc
     * Includes the access token obtained from the Keycloak client service in the request headers.
     */
    @Override
    public HttpEntity<Map<String, Object>> buildHttpRequestEntity(
            String clientId,
            Map<String, Object> requestBody,
            String tokenEndpointUrl
    ) {
        return new HttpEntity<>(
                requestBody,
                buildHeaders(
                        clientService.generateAccessToken(
                                kcAdminCliClientId,
                                tokenEndpointUrl
                        ).getToken()
                )
        );
    }

    @Override
    public HttpEntity buildOnlyAuthHttpRequestEntity(
            String clientId,
            String tokenEndpointUrl
    ) {
        return new HttpEntity(
                buildEmptyRequestBody(),
                buildHeaders(
                        clientService.generateAccessToken(
                                kcAdminCliClientId,
                                tokenEndpointUrl
                        ).getToken()
                )
        );
    }

    /**
     * @inheritDoc
     */
    @Override
    public String buildApiBaseUrl (
            String kcBaseUrl,
            String kcRealm
    ) {
        return "http://" + kcBaseUrl + "/admin/realms/" + kcRealm;
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

    private ObjectNode buildEmptyRequestBody() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.createObjectNode();
    }
}
