package com.iliauni.usersyncglobalservice.idp.ipa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.usersyncglobalservice.exception.*;
import com.iliauni.usersyncglobalservice.idp.IdpJsonObjectMapper;
import com.iliauni.usersyncglobalservice.idp.IdpRequestBuilder;
import com.iliauni.usersyncglobalservice.idp.IdpUserRequestSender;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * A component class implementing the {@link IdpUserRequestSender} interface for sending user-related requests in an Identity Provider (IDP) context specific to FreeIPA (Identity, Policy, and Audit) systems.
 */
@Component
public class IpaIdpUserRequestSender implements IdpUserRequestSender<IpaClient> {
    private final IdpRequestBuilder<IpaClient> requestBuilder;
    private final IdpJsonObjectMapper jsonObjectMapper;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${ipaApiEndpoint}")
    private String ipaApiEndpoint;

    @Value("${ipaApiAuthEndpoint}")
    private String ipaAuthEndpoint;

    /**
     * Constructs an {@code IpaIdpUserRequestSender} instance with the specified {@link IdpRequestBuilder}, {@link IdpJsonObjectMapper}, and {@link RestTemplateBuilder}.
     *
     * @param requestBuilder the request builder for FreeIPA IDP
     * @param objectMapper the object mapper for FreeIPA IDP
     */
    @Autowired
    public IpaIdpUserRequestSender(
            IdpRequestBuilder<IpaClient> requestBuilder,
            @Qualifier("ipaIdpJsonObjectMapper") IdpJsonObjectMapper jsonObjectMapper,
            ObjectMapper objectMapper
    ) {
        this.requestBuilder = requestBuilder;
        this.jsonObjectMapper = jsonObjectMapper;
        this.objectMapper = objectMapper;
        this.restTemplate = requestBuilder.getRestTemplate();
    }

    @Override
    public User sendCreateUserRequest(
            IpaClient client,
            User user
    ) throws UserToJsonMappingException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "user_add");
        try {
            requestBody.put("params", new Object[]{"", jsonObjectMapper.mapUserToJsonString(user)});
        } catch (JsonProcessingException exception) {
            throw new UserToJsonMappingException(
                    "An exception occurred while mapping user to JSON string: "
                            + exception.getMessage()
            );
        }

        sendRestTemplateRequest(
                client,
                requestBody
        );

        return user;
    }

    @Override
    public JsonNode sendGetUserRequest(
            IpaClient client,
            String username
    ) throws GetUserRequestJsonReadingException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "user_show");
        requestBody.put("params", new Object[]{username, new HashMap<>()});

        try {
            return objectMapper.readTree(
                    sendRestTemplateRequest(
                            client,
                            requestBody
                    ));
        } catch (JsonProcessingException exception) {
            throw new GetUserRequestJsonReadingException(
                    "An exception occurred while reading JSON received from the request to retrieve a user for FreeIPA client with id "
                            + client.getId() + ": "
                            + exception.getMessage()
            );
        }
    }

    @Override
    public JsonNode sendGetUsersRequest(IpaClient client)
            throws GetUsersRequestJsonReadingException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "user_find");
        requestBody.put("params", new Object[]{"", new HashMap<>()});

        try {
            return objectMapper.readTree(
                    sendRestTemplateRequest(
                            client,
                            requestBody
                    ));
        } catch (JsonProcessingException exception) {
            throw new GetUsersRequestJsonReadingException(
                    "An exception occurred while reading JSON received from the request to retrieve users for FreeIPA client with id "
                            + client.getId() + ": "
                            + exception.getMessage()
            );
        }
    }

    @Override
    public String sendUpdateUserPasswordRequest(
            IpaClient client,
            String username,
            String newPassword
    ) throws CredentialsRepresentationBuildingException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "passwd");
        try {
            requestBody.put("params", new Object[]{
                    username,
                    jsonObjectMapper.buildCredentialsRepresentation(newPassword)
            });
        } catch (JsonProcessingException exception) {
            throw new CredentialsRepresentationBuildingException(
                    "An exception occurred while building credentials representation: "
                            + exception.getMessage()
            );
        }

        sendRestTemplateRequest(
                client,
                requestBody
        );

        return newPassword;
    }

    @Override
    public void sendDeleteUserRequest(
            IpaClient client,
            String username
    ) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "user_del");
        requestBody.put("params", new Object[]{username, new HashMap<>()});

        sendRestTemplateRequest(
                client,
                requestBody
        );
    }

    private String sendRestTemplateRequest(
            IpaClient client,
            Map<String, Object> requestBody
    ) {
        try {
            return restTemplate.exchange(
                    requestBuilder.buildRequestUrl(
                            client,
                            "https",
                            ipaApiEndpoint
                    ),
                    HttpMethod.POST,
                    requestBuilder.buildHttpRequestEntity(
                            client.getId(),
                            objectMapper.writeValueAsString(requestBody),
                            ipaAuthEndpoint
                    ),
                    String.class
            ).getBody();
        } catch (JsonProcessingException exception) {
            throw new WritingRequestBodyToStringException(
                    "An exception occurred while writing request body to a string: "
                            + exception.getMessage()
            );
        }
    }
}
