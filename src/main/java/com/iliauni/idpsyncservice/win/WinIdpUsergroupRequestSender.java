package com.iliauni.idpsyncservice.win;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.idpsyncservice.exception.UsergroupJsonReadingException;
import com.iliauni.idpsyncservice.exception.UsergroupMembersJsonReadingException;
import com.iliauni.idpsyncservice.exception.UsergroupsJsonReadingException;
import com.iliauni.idpsyncservice.idp.IdpJsonObjectMapper;
import com.iliauni.idpsyncservice.idp.IdpRequestBuilder;
import com.iliauni.idpsyncservice.idp.IdpUsergroupRequestSender;
import com.iliauni.idpsyncservice.model.Usergroup;
import com.iliauni.idpsyncservice.model.WinClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WinIdpUsergroupRequestSender implements IdpUsergroupRequestSender<WinClient> {
    private final IdpRequestBuilder<WinClient> requestBuilder;
    private final IdpJsonObjectMapper jsonObjectMapper;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Autowired
    public WinIdpUsergroupRequestSender(
            IdpRequestBuilder<WinClient> requestBuilder,
            @Qualifier("winIdpJsonObjectMapper") IdpJsonObjectMapper jsonObjectMapper,
            ObjectMapper objectMapper
    ) {
        this.requestBuilder = requestBuilder;
        this.jsonObjectMapper = jsonObjectMapper;
        this.objectMapper = objectMapper;
        // pass null as RestTemplate building for Win client doesn't depend on the client
        this.restTemplate = requestBuilder.getRestTemplate(null);
    }

    @Override
    public Usergroup sendCreateUsergroupRequest(
            WinClient client,
            Usergroup usergroup
    ) {
        restTemplate.exchange(
                requestBuilder.buildRequestUrl(
                        client,
                        "http",
                        "/groups/create/"
                ),
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        jsonObjectMapper.mapUsergroupToJsonString(usergroup),
                        requestBuilder.buildAuthRequestUrl(
                                client,
                                "http"
                        )
                ),
                String.class
            );

        return usergroup;
    }

    @Override
    public void sendAddUsergroupMemberRequest(
            WinClient client,
            String usergroupName,
            String username
    ) {
        restTemplate.exchange(
                requestBuilder.buildRequestUrl(
                        client,
                        "http",
                        "/groups/add-user/"
                                + usergroupName + "/"
                                + username + "/"
                ),
                HttpMethod.PATCH,
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

    @Override
    public JsonNode sendGetUsergroupRequest(
            WinClient client,
            String usergroupName
    ) {
        try {
            return objectMapper.readTree(
                    restTemplate.exchange(
                            requestBuilder.buildRequestUrl(
                                    client,
                                    "http",
                                    "/groups/"
                                            + usergroupName + "/"
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
            throw new UsergroupJsonReadingException(
                    "An exception occurred while reading JSON received from the request to retrieve a user group for Windows client with id "
                            + client.getId() + ": "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    @Override
    public JsonNode sendGetUsergroupsRequest(WinClient client) {
        try {
            return objectMapper.readTree(
                    restTemplate.exchange(
                            requestBuilder.buildRequestUrl(
                                    client,
                                    "http",
                                    "/groups/"
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
            throw new UsergroupsJsonReadingException(
                    "An exception occurred while reading JSON received from the request to retrieve user groups for Windows client with id "
                            + client.getId() + ": "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    @Override
    public JsonNode sendGetUsergroupMembersRequest(
            WinClient client,
            String usergroupName
    ) {
        try {
            return objectMapper.readTree(
                    restTemplate.exchange(
                            requestBuilder.buildRequestUrl(
                                    client,
                                    "http",
                                    "/groups/"
                                            + usergroupName
                                            + "/users/"
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
            throw new UsergroupMembersJsonReadingException(
                    "An exception occurred while reading JSON received from the request to retrieve user group members for Windows client with id "
                            + client.getId() + ": "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    @Override
    public void sendDeleteUsergroupRequest(
            WinClient client,
            String usergroupName
    ) {
        restTemplate.exchange(
                requestBuilder.buildRequestUrl(
                        client,
                        "http",
                        "/groups/delete/"
                                + usergroupName + "/"
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

    @Override
    public void sendRemoveUsergroupMemberRequest(
            WinClient client,
            String usergroupName,
            String username
    ) {
        restTemplate.exchange(
                requestBuilder.buildRequestUrl(
                        client,
                        "http",
                        "/groups/remove-user/"
                                + usergroupName + "/"
                                + username + "/"
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
