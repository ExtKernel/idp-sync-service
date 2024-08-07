package com.iliauni.idpsyncservice.kc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.idpsyncservice.exception.KcUserWithUsernameNotFoundException;
import com.iliauni.idpsyncservice.exception.UserJsonReadingException;
import com.iliauni.idpsyncservice.exception.UsersJsonReadingException;
import com.iliauni.idpsyncservice.idp.IdpJsonObjectMapper;
import com.iliauni.idpsyncservice.idp.IdpRequestBuilder;
import com.iliauni.idpsyncservice.idp.IdpUserRequestSender;
import com.iliauni.idpsyncservice.model.SyncKcClient;
import com.iliauni.idpsyncservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * A component class implementing the {@link IdpUserRequestSender} interface
 * for sending user-related requests in an Identity Provider (IDP) context specific to Keycloak (KC) systems.
 */
@Component
public class SyncKcIdpUserRequestSender implements IdpUserRequestSender<SyncKcClient> {
    private final IdpRequestBuilder<SyncKcClient> requestBuilder;
    private final IdpJsonObjectMapper jsonObjectMapper;
    private final SyncKcIdpModelIdExtractor idExtractor;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${kcAdminCliClientId}")
    private String kcAdminCliClientId;

    @Autowired
    public SyncKcIdpUserRequestSender(
            IdpRequestBuilder<SyncKcClient> requestBuilder,
            @Qualifier("syncKcIdpJsonObjectMapper") IdpJsonObjectMapper jsonObjectMapper,
            SyncKcIdpModelIdExtractor idExtractor,
            ObjectMapper objectMapper
    ) {
        this.requestBuilder = requestBuilder;
        this.jsonObjectMapper = jsonObjectMapper;
        // pass null as RestTemplate building for SyncKc client doesn't depend on the client
        this.restTemplate = requestBuilder.getRestTemplate(null);
        this.idExtractor = idExtractor;
        this.objectMapper = objectMapper;
    }

    @Override
    public User sendCreateUserRequest(
            SyncKcClient client,
            User user
    ) {
        restTemplate.exchange(
                requestBuilder.buildRequestUrl(
                        client,
                        "http",
                        "/users"
                ),
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        jsonObjectMapper.mapUserToJsonString(user),
                        requestBuilder.buildAuthRequestUrl(
                                client,
                                "http"
                        )
                ),
                String.class
        );

        return user;
    }

    @Override
    public JsonNode sendGetUserRequest(
            SyncKcClient client,
            String username
    ) {
        try {
            return objectMapper.readTree(
                    restTemplate.exchange(
                            requestBuilder.buildRequestUrl(
                                    client,
                                    "http",
                                    "/users/"
                                            + idExtractor.getUserId(sendGetUsersRequest(client), username)
                            ),
                            HttpMethod.GET,
                            requestBuilder.buildAuthOnlyHttpRequestEntity(
                                    kcAdminCliClientId,
                                    requestBuilder.buildAuthRequestUrl(
                                            client,
                                            "http"
                                    )
                            ),
                            String.class
                    ).getBody()
            );
        } catch (KcUserWithUsernameNotFoundException exception) {
            return null;
        } catch (JsonProcessingException exception) {
            throw new UserJsonReadingException(
                    "An exception occurred while reading JSON received from the request"
                            + " to retrieve a user for a Keycloak client with id "
                            + client.getId() + ": "
                            + exception.getMessage()
            );
        }
    }

    @Cacheable("syncKcUsers")
    @Override
    public JsonNode sendGetUsersRequest(SyncKcClient client) {
        try {
            return objectMapper.readTree(
                    restTemplate.exchange(
                            requestBuilder.buildRequestUrl(
                                    client,
                                    "http",
                                    "/users/"
                            ),
                            HttpMethod.GET,
                            requestBuilder.buildAuthOnlyHttpRequestEntity(
                                    kcAdminCliClientId,
                                    requestBuilder.buildAuthRequestUrl(
                                            client,
                                            "http"
                                    )
                            ),
                            String.class
                    ).getBody()
            );
        } catch (JsonProcessingException exception) {
            throw new UsersJsonReadingException(
                    "An exception occurred while reading JSON received from the request"
                            + " to retrieve users for a Keycloak client with id "
                            + client.getId() + ": "
                            + exception.getMessage()
            );
        }
    }

    @Override
    public String sendUpdateUserPasswordRequest(
            SyncKcClient client,
            String username,
            String newPassword
    ) {
        restTemplate.exchange(
                requestBuilder.buildRequestUrl(
                        client,
                        "http",
                        "/users/"
                                + idExtractor.getUserId(sendGetUsersRequest(client), username)
                                + "/reset-password"
                ),
                HttpMethod.PUT,
                requestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        jsonObjectMapper.buildCredentialsRepresentation(newPassword),
                        requestBuilder.buildAuthRequestUrl(
                                client,
                                "http"
                        )
                ),
                String.class
        );

        return newPassword;
    }

    @Override
    public void sendDeleteUserRequest(
            SyncKcClient client,
            String username
    ) {
        restTemplate.exchange(
                requestBuilder.buildRequestUrl(
                        client,
                        "http",
                        "/users/"
                                + idExtractor.getUserId(sendGetUsersRequest(client), username)
                ),
                HttpMethod.DELETE,
                requestBuilder.buildAuthOnlyHttpRequestEntity(
                        kcAdminCliClientId,
                        requestBuilder.buildAuthRequestUrl(
                                client,
                                "http"
                        )
                ),
                String.class
        );
    }
}
