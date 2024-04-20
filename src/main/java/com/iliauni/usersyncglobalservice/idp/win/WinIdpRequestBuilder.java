package com.iliauni.usersyncglobalservice.idp.win;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iliauni.usersyncglobalservice.idp.IdpRequestBuilder;
import com.iliauni.usersyncglobalservice.model.WinClient;
import com.iliauni.usersyncglobalservice.service.Oauth2ClientService;
import com.iliauni.usersyncglobalservice.service.WinClientService;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class WinIdpRequestBuilder implements IdpRequestBuilder {
    private final Oauth2ClientService<WinClient> clientService;

    @Autowired
    public WinIdpRequestBuilder(WinClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public HttpEntity<String> buildHttpRequestEntity(
            String clientId,
            Map<String, Object> requestBody,
            String tokenEndpointUrl
    ) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return new HttpEntity<>(
                    objectMapper.writeValueAsString(requestBody),
                    buildHeaders(
                        clientService.generateAccessToken(
                                clientId,
                                tokenEndpointUrl
                        ).getToken()
                    )
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
                                clientId,
                                tokenEndpointUrl
                        ).getToken()
                )
        );
    }

    @Override
    public String buildApiBaseUrl(
            String hostname,
            String endpoint
    ) {
        return "http://" + hostname + endpoint;
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
