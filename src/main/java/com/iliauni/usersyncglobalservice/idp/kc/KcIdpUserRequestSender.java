package com.iliauni.usersyncglobalservice.idp.kc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.usersyncglobalservice.exception.*;
import com.iliauni.usersyncglobalservice.idp.IdpJsonObjectMapper;
import com.iliauni.usersyncglobalservice.idp.IdpRequestBuilder;
import com.iliauni.usersyncglobalservice.idp.IdpUserRequestSender;
import com.iliauni.usersyncglobalservice.model.KcClient;
import com.iliauni.usersyncglobalservice.model.User;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * A component class implementing the {@link IdpUserRequestSender} interface for sending user-related requests in an Identity Provider (IDP) context specific to Keycloak (KC) systems.
 */
@Component
public class KcIdpUserRequestSender implements IdpUserRequestSender<KcClient> {
    private final IdpRequestBuilder<KcClient> requestBuilder;
    private final IdpJsonObjectMapper jsonObjectMapper;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${kcAdminCliClientId}")
    private String kcAdminCliClientId;

    /**
     * Constructs an {@code KcIdpUserRequestSender} instance with the specified {@link IdpRequestBuilder}, {@link IdpJsonObjectMapper}, and {@link RestTemplateBuilder}.
     *
     * @param requestBuilder the request builder for Keycloak IDP
     * @param objectMapper the object mapper for Keycloak IDP
     */
    @Autowired
    public KcIdpUserRequestSender(
            IdpRequestBuilder<KcClient> requestBuilder,
            @Qualifier("kcIdpJsonObjectMapper") IdpJsonObjectMapper jsonObjectMapper,
            ObjectMapper objectMapper
    ) {
        this.requestBuilder = requestBuilder;
        this.jsonObjectMapper = jsonObjectMapper;
        this.restTemplate = requestBuilder.getRestTemplate();
        this.objectMapper = objectMapper;
    }

    @Override
    public User sendCreateUserRequest(
            KcClient client,
            User user
    ) throws UserToJsonMappingException {
        try {
            restTemplate.exchange(
                    requestBuilder.buildRequestUrl(
                            client,
                            "http",
                            "/admin/realms/"
                                    + client.getRealm()
                                    + "/users"
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
        } catch (JsonProcessingException exception) {
            throw new UserToJsonMappingException(
                    "An exception occurred while mapping user to JSON string: "
                            + exception.getMessage()
            );
        }

        return user;
    }

    @Override
    public JsonNode sendGetUserRequest(
            KcClient client,
            String username
    ) throws GetUserRequestJsonReadingException {
        try {
            return objectMapper.readTree(
                    restTemplate.exchange(
                            requestBuilder.buildRequestUrl(
                                    client,
                                    "http",
                                    "/admin/realms/"
                                            + client.getRealm()
                                            + "/users/"
                                            + getUserId(client, username)
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
            throw new GetUserRequestJsonReadingException(
                    "An exception occurred while reading JSON received from the request to retrieve a user for Keycloak client with id "
                            + client.getId() + ": "
                            + exception.getMessage()
            );        }
    }

    @Override
    public JsonNode sendGetUsersRequest(KcClient client)
            throws GetUsersRequestJsonReadingException {
        try {
            return objectMapper.readTree(
                    restTemplate.exchange(
                            requestBuilder.buildRequestUrl(
                                    client,
                                    "http",
                                    "/admin/realms/"
                                            + client.getRealm()
                                            + "/users/"
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
            throw new GetUsersRequestJsonReadingException(
                    "An exception occurred while reading JSON received from the request to retrieve users for Keycloak client with id "
                            + client.getId() + ": "
                            + exception.getMessage()
            );        }
    }

    @Override
    public String sendUpdateUserPasswordRequest(
            KcClient client,
            String username,
            String newPassword
    ) throws CredentialsRepresentationBuildingException {
        try {
            restTemplate.exchange(
                    requestBuilder.buildRequestUrl(
                            client,
                            "http",
                            "/admin/realms/"
                                    + client.getRealm()
                                    + "/users/"
                                    + getUserId(client, username)
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
        } catch (JsonProcessingException exception) {
            throw new CredentialsRepresentationBuildingException(
                    "An exception occurred while building credentials representation: "
                            + exception.getMessage()
            );
        }

        return newPassword;
    }

    @Override
    public void sendDeleteUserRequest(
            KcClient client,
            String username
    ) {
        restTemplate.exchange(
                requestBuilder.buildRequestUrl(
                        client,
                        "http",
                        "/admin/realms/"
                                + client.getRealm()
                                + "/users/"
                                + getUserId(client, username)
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

    /**
     * Retrieves the user ID for the given username using the Keycloak client.
     * Throws an exception if the user with the specified username is not found.
     *
     * @param client the Keycloak client
     * @param username the username of the user
     * @return the user ID
     * @throws KcUserWithUsernameNotFoundException if the user with the specified username is not found
     */
    private String getUserId(
            KcClient client,
            String username
    ) throws KcUserWithUsernameNotFoundException {
        JsonNode users;
        users = sendGetUsersRequest(client);

        for (JsonNode user : users) {
            if (Objects.equals(user.get("username").toString(), username)) {
                return user.get("id").toString();
            }
        }
        throw new KcUserWithUsernameNotFoundException(
                "Keycloak user with username " + username + " was not found");
    }
}
