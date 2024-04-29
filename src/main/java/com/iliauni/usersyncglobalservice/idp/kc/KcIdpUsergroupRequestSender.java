package com.iliauni.usersyncglobalservice.idp.kc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.usersyncglobalservice.exception.*;
import com.iliauni.usersyncglobalservice.idp.IdpJsonObjectMapper;
import com.iliauni.usersyncglobalservice.idp.IdpRequestBuilder;
import com.iliauni.usersyncglobalservice.idp.IdpUserRequestSender;
import com.iliauni.usersyncglobalservice.idp.IdpUsergroupRequestSender;
import com.iliauni.usersyncglobalservice.model.KcClient;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * A component class implementing the {@link IdpUsergroupRequestSender} interface for sending requests related to user groups in an Identity Provider (IDP) context specific to Keycloak (KC) systems.
*/
@Component
public class KcIdpUsergroupRequestSender implements IdpUsergroupRequestSender<KcClient> {
    private final IdpRequestBuilder<KcClient> requestBuilder;
    private final IdpJsonObjectMapper jsonObjectMapper;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${kcAdminCliClientId}")
    private String kcAdminCliClientId;

    /**
     * Constructs a {@code KcIdpUsergroupRequestSender} instance with the specified {@link IdpRequestBuilder}, {@link IdpJsonObjectMapper}, and {@link RestTemplateBuilder}.
     *
     * @param requestBuilder the request builder for Keycloak IDP
     * @param objectMapper the object mapper for Keycloak IDP
     * @param restTemplateBuilder the builder for creating RestTemplate
     */
    @Autowired
    public KcIdpUsergroupRequestSender(
            IdpRequestBuilder<KcClient> requestBuilder,
            @Qualifier("kcIdpJsonObjectMapper") IdpJsonObjectMapper jsonObjectMapper,
            ObjectMapper objectMapper,
            RestTemplateBuilder restTemplateBuilder) {
        this.requestBuilder = requestBuilder;
        this.jsonObjectMapper = jsonObjectMapper;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
    }

    @Override
    public Usergroup sendCreateUsergroupRequest(
            KcClient client,
            Usergroup usergroup
    ) throws UsergroupToJsonMappingException {
        try {
            restTemplate.exchange(
                    requestBuilder.buildRequestUrl(
                            client,
                            "http",
                            "/admin/realms/"
                                    + client.getRealm()
                                    + "/groups"
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
        } catch (JsonProcessingException exception) {
            throw new UsergroupToJsonMappingException(
                    "An exception occurred while mapping user group to JSON string: "
                            + exception.getMessage()
            );
        }

        return usergroup;
    }

    @Override
    public void sendAddUsergroupMemberRequest(
            KcClient client,
            String usergroupId,
            String userId
    ) {
        restTemplate.exchange(
                requestBuilder.buildRequestUrl(
                        client,
                        "http",
                        "/users/"
                                + userId
                                + "/groups/"
                                + usergroupId
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
            KcClient client,
            String usergroupName
    ) throws GetUsergroupRequestJsonReadingException {
        try {
            return objectMapper.readTree(
                    restTemplate.exchange(
                            requestBuilder.buildRequestUrl(
                                    client,
                                    "http",
                                    "/admin/realms/"
                                            + client.getRealm()
                                            + "/groups/"
                                            + getUsergroupId(client, usergroupName)
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
            throw new GetUsergroupRequestJsonReadingException(
                    "An exception occurred while reading JSON received from the request to retrieve a user group for Keycloak client with id "
                            + client.getId() + ": "
                            + exception.getMessage()
            );
        }
    }

    @Override
    public JsonNode sendGetUsergroupsRequest(KcClient client)
            throws GetUsergroupsRequestJsonReadingException {
        try {
            return objectMapper.readTree(
                    restTemplate.exchange(
                            requestBuilder.buildRequestUrl(
                                    client,
                                    "http",
                                    "/admin/realms/"
                                            + client.getRealm()
                                            + "/groups"
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
            throw new GetUsergroupsRequestJsonReadingException(
                    "An exception occurred while reading JSON received from the request to retrieve user groups for Keycloak client with id "
                            + client.getId() + ": "
                            + exception.getMessage()
            );
        }
    }

    @Override
    public JsonNode sendGetUsergroupMembersRequest(
            KcClient client,
            String usergroupName
    ) throws GetUsergroupMembersRequestJsonReadingException {
        try {
            return objectMapper.readTree(
                    restTemplate.exchange(
                            requestBuilder.buildRequestUrl(
                                    client,
                                    "http",
                                    "/groups/"
                                            + getUsergroupId(client, usergroupName)
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
            throw new GetUsergroupMembersRequestJsonReadingException(
                    "An exception occurred while reading JSON received from the request to retrieve user group members for Keycloak client with id "
                            + client.getId() + ": "
                            + exception.getMessage()
            );
        }
    }

    @Override
    public void sendDeleteUsergroupRequest(
            KcClient client,
            String usergroupName
    ) {
        restTemplate.exchange(
                requestBuilder.buildRequestUrl(
                        client,
                        "http",
                        "/admin/realms/"
                                + client.getRealm()
                                + "/groups"
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
            KcClient client,
            String usergroupName,
            String userId
    ) {
        restTemplate.exchange(
                requestBuilder.buildRequestUrl(
                        client,
                        "http",
                        "/users/"
                                + userId
                                + "/groups/"
                                + getUsergroupId(client, usergroupName)
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
     * Retrieves the ID of the user group with the specified name using the Keycloak client.
     *
     * @param client the Keycloak client
     * @param usergroupName the name of the user group
     * @return the ID of the user group
     * @throws KcUsergroupWithNameNotFoundException if the user group with the specified name is not found
     */
    private String getUsergroupId(
            KcClient client,
            String usergroupName
    ) {
        JsonNode usergroups;
        usergroups = sendGetUsergroupsRequest(client);

        for (JsonNode usergroup : usergroups) {
            if (usergroup.get("name").asText().equals(usergroupName)) {
                return usergroup.path("id").asText();
            }
        }
        throw new KcUsergroupWithNameNotFoundException("Keycloak user group with name " + usergroupName + " was not found");
    }
}
