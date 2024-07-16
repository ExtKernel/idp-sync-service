package com.iliauni.idpsyncservice.kc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.idpsyncservice.exception.UsergroupJsonReadingException;
import com.iliauni.idpsyncservice.exception.UsergroupMembersJsonReadingException;
import com.iliauni.idpsyncservice.exception.UsergroupsJsonReadingException;
import com.iliauni.idpsyncservice.idp.IdpJsonObjectMapper;
import com.iliauni.idpsyncservice.idp.IdpRequestBuilder;
import com.iliauni.idpsyncservice.idp.IdpUserRequestSender;
import com.iliauni.idpsyncservice.idp.IdpUsergroupRequestSender;
import com.iliauni.idpsyncservice.model.SyncKcClient;
import com.iliauni.idpsyncservice.model.Usergroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * A component class implementing the {@link IdpUsergroupRequestSender} interface
 * for sending requests related to user groups in an Identity Provider (IDP) context
 * specific to Keycloak (KC) systems.
*/
@Component
public class SyncKcIdpUsergroupRequestSender implements IdpUsergroupRequestSender<SyncKcClient> {
    private final IdpRequestBuilder<SyncKcClient> requestBuilder;
    private final IdpJsonObjectMapper jsonObjectMapper;
    private final IdpUserRequestSender<SyncKcClient> userRequestSender;
    private final SyncKcIdpModelIdExtractor idExtractor;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${kcAdminCliClientId}")
    private String kcAdminCliClientId;

    @Autowired
    public SyncKcIdpUsergroupRequestSender(
            IdpRequestBuilder<SyncKcClient> requestBuilder,
            @Qualifier("syncKcIdpJsonObjectMapper") IdpJsonObjectMapper jsonObjectMapper,
            @Lazy IdpUserRequestSender<SyncKcClient> userRequestSender,
            SyncKcIdpModelIdExtractor idExtractor,
            ObjectMapper objectMapper
    ) {
        this.requestBuilder = requestBuilder;
        this.jsonObjectMapper = jsonObjectMapper;
        this.userRequestSender = userRequestSender;
        this.idExtractor = idExtractor;
        this.objectMapper = objectMapper;
        // pass null as RestTemplate building for SyncKc client doesn't depend on the client
        this.restTemplate = requestBuilder.getRestTemplate(null);
    }

    @Override
    public Usergroup sendCreateUsergroupRequest(
            SyncKcClient client,
            Usergroup usergroup
    ) {
        restTemplate.exchange(
                requestBuilder.buildRequestUrl(
                        client,
                        "http",
                        "/groups"
                ),
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(
                        kcAdminCliClientId,
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
            SyncKcClient client,
            String usergroupName,
            String username
    ) {
        restTemplate.exchange(
                requestBuilder.buildRequestUrl(
                        client,
                        "http",
                        "/users/"
                                + idExtractor.getUserId(
                                        userRequestSender.sendGetUsersRequest(client),
                                        username
                                )
                                + "/groups/"
                                + idExtractor.getUsergroupId(
                                        sendGetUsergroupsRequest(client),
                                        usergroupName
                                )
                ),
                HttpMethod.PUT,
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

    @Override
    public JsonNode sendGetUsergroupRequest(
            SyncKcClient client,
            String usergroupName
    ) {
        try {
            return objectMapper.readTree(
                    restTemplate.exchange(
                            requestBuilder.buildRequestUrl(
                                    client,
                                    "http",
                                    "/groups/"
                                            + idExtractor.getUsergroupId(
                                                    sendGetUsergroupsRequest(client),
                                                    usergroupName
                                            )
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
            throw new UsergroupJsonReadingException(
                    "An exception occurred while reading JSON received from the request"
                            + " to retrieve a user group for Keycloak client with id "
                            + client.getId() + ": "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    @Override
    public JsonNode sendGetUsergroupsRequest(SyncKcClient client) {
        try {
            return objectMapper.readTree(
                    restTemplate.exchange(
                            requestBuilder.buildRequestUrl(
                                    client,
                                    "http",
                                    "/groups"
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
            throw new UsergroupsJsonReadingException(
                    "An exception occurred while reading JSON received from the request to retrieve"
                            + " user groups for Keycloak client with id "
                            + client.getId() + ": "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    @Override
    public JsonNode sendGetUsergroupMembersRequest(
            SyncKcClient client,
            String usergroupName
    ) {
        try {
            return objectMapper.readTree(
                    restTemplate.exchange(
                            requestBuilder.buildRequestUrl(
                                    client,
                                    "http",
                                    "/groups/"
                                            + idExtractor.getUsergroupId(
                                                    sendGetUsergroupsRequest(client),
                                                    usergroupName
                                            )
                                            + "/members"
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
            throw new UsergroupMembersJsonReadingException(
                    "An exception occurred while reading JSON received from the request"
                            + " to retrieve user group members for Keycloak client with id "
                            + client.getId() + ": "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    @Override
    public void sendDeleteUsergroupRequest(
            SyncKcClient client,
            String usergroupName
    ) {
        restTemplate.exchange(
                requestBuilder.buildRequestUrl(
                        client,
                        "http",
                        "/groups/"
                                + idExtractor.getUsergroupId(
                                        sendGetUsergroupsRequest(client),
                                        usergroupName
                                )
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

    @Override
    public void sendRemoveUsergroupMemberRequest(
            SyncKcClient client,
            String usergroupName,
            String username
    ) {
        restTemplate.exchange(
                requestBuilder.buildRequestUrl(
                        client,
                        "http",
                        "/users/"
                                + idExtractor.getUserId(
                                        userRequestSender.sendGetUsersRequest(client),
                                        username
                                )
                                + "/groups/"
                                + idExtractor.getUsergroupId(
                                        sendGetUsergroupsRequest(client),
                                        usergroupName
                                )
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
