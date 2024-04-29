package com.iliauni.usersyncglobalservice.idp.win;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.usersyncglobalservice.exception.CredentialsRepresentationBuildingException;
import com.iliauni.usersyncglobalservice.exception.GetUserRequestJsonReadingException;
import com.iliauni.usersyncglobalservice.exception.GetUsersRequestJsonReadingException;
import com.iliauni.usersyncglobalservice.exception.UserToJsonMappingException;
import com.iliauni.usersyncglobalservice.idp.IdpJsonObjectMapper;
import com.iliauni.usersyncglobalservice.idp.IdpRequestBuilder;
import com.iliauni.usersyncglobalservice.idp.IdpUserRequestSender;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.WinClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WinIdpUserRequestSender implements IdpUserRequestSender<WinClient> {
    private final IdpRequestBuilder<WinClient> requestBuilder;
    private final IdpJsonObjectMapper jsonObjectMapper;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Autowired
    public WinIdpUserRequestSender(
            IdpRequestBuilder<WinClient> requestBuilder,
            @Qualifier("winIdpJsonObjectMapper") IdpJsonObjectMapper jsonObjectMapper,
            ObjectMapper objectMapper
    ) {
        this.requestBuilder = requestBuilder;
        this.jsonObjectMapper = jsonObjectMapper;
        this.restTemplate = requestBuilder.getRestTemplate();
        this.objectMapper = objectMapper;
    }

    @Override
    public User sendCreateUserRequest(
            WinClient client,
            User user
    ) throws UserToJsonMappingException {
        try {
            restTemplate.exchange(
                    requestBuilder.buildRequestUrl(
                            client,
                            "http",
                            "/users/create/"
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
            WinClient client,
            String username
    ) throws GetUserRequestJsonReadingException {
        try {
            return objectMapper.readTree(
                    restTemplate.exchange(
                            requestBuilder.buildRequestUrl(
                                    client,
                                    "http",
                                    "/users/" + username + "/"
                            ),
                            HttpMethod.GET,
                            requestBuilder.buildAuthOnlyHttpRequestEntity(
                                    client.getId(),
                                    requestBuilder.buildAuthRequestUrl(
                                            client,
                                            "http"
                                    )
                            ),
                            String.class
                    ).getBody()
            );
        } catch (JsonProcessingException exception) {
            throw new GetUserRequestJsonReadingException(
                    "An exception occurred while reading JSON received from the request to retrieve a user for Windows client with id "
                            + client.getId() + ": "
                            + exception.getMessage()
            );
        }
    }

    @Override
    public JsonNode sendGetUsersRequest(WinClient client)
            throws GetUsersRequestJsonReadingException {
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
                                    client.getId(),
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
                    "An exception occurred while reading JSON received from the request to retrieve users for Windows client with id "
                            + client.getId() + ": "
                            + exception.getMessage()
            );
        }
    }

    @Override
    public String sendUpdateUserPasswordRequest(
            WinClient client,
            String username,
            String newPassword
    ) {
        try {
            restTemplate.exchange(
                    requestBuilder.buildRequestUrl(
                            client,
                            "http",
                            "/users/update-password/" + username
                    ),
                    HttpMethod.PATCH,
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
            WinClient client,
            String username
    ) {
        restTemplate.exchange(
                requestBuilder.buildRequestUrl(
                        client,
                        "http",
                        "/users/delete/" + username
                ),
                HttpMethod.DELETE,
                requestBuilder.buildAuthOnlyHttpRequestEntity(
                        client.getId(),
                        requestBuilder.buildAuthRequestUrl(
                                client,
                                "http"
                        )
                ),
                String.class
        );
    }
}
