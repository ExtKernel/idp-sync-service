package com.iliauni.idpsyncservice.ipa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.idpsyncservice.exception.UserJsonReadingException;
import com.iliauni.idpsyncservice.exception.UsersJsonReadingException;
import com.iliauni.idpsyncservice.exception.WritingRequestBodyToStringException;
import com.iliauni.idpsyncservice.idp.IdpJsonObjectMapper;
import com.iliauni.idpsyncservice.idp.IdpRequestBuilder;
import com.iliauni.idpsyncservice.idp.IdpUserRequestSender;
import com.iliauni.idpsyncservice.model.IpaClient;
import com.iliauni.idpsyncservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * A component class implementing the {@link IdpUserRequestSender} interface for sending user-related requests
 * in an Identity Provider (IDP) context specific to FreeIPA (Identity, Policy, and Audit) systems.
 */
@Component
public class IpaIdpUserRequestSender implements IdpUserRequestSender<IpaClient> {
    private final IdpRequestBuilder<IpaClient> requestBuilder;
    private final IdpJsonObjectMapper jsonObjectMapper;
    private final ObjectMapper objectMapper;

    @Value("${ipaApiEndpoint}")
    private String ipaApiEndpoint;

    @Value("${ipaApiAuthEndpoint}")
    private String ipaAuthEndpoint;

    @Autowired
    public IpaIdpUserRequestSender(
            IdpRequestBuilder<IpaClient> requestBuilder,
            @Qualifier("ipaIdpJsonObjectMapper") IdpJsonObjectMapper jsonObjectMapper,
            ObjectMapper objectMapper
    ) {
        this.requestBuilder = requestBuilder;
        this.jsonObjectMapper = jsonObjectMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public User sendCreateUserRequest(
            IpaClient client,
            User user
    ) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "user_add");
        requestBody.put("params", new Object[]{"", jsonObjectMapper.mapUserToJsonString(user)});

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
    ) {
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
            throw new UserJsonReadingException(
                    "An exception occurred while reading JSON received from the request"
                            + " to retrieve a user for a FreeIPA client with id "
                            + client.getId() + ": "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    @Override
    public JsonNode sendGetUsersRequest(IpaClient client) {
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
            throw new UsersJsonReadingException(
                    "An exception occurred while reading JSON received from the request"
                            + " to retrieve users for a FreeIPA client with id "
                            + client.getId() + ": "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    @Override
    public String sendUpdateUserPasswordRequest(
            IpaClient client,
            String username,
            String newPassword
    ) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "passwd");
        requestBody.put("params", new Object[]{
                username,
                jsonObjectMapper.buildCredentialsRepresentation(newPassword)
        });

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
            return requestBuilder.getRestTemplate(client).exchange(
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
                            + exception.getMessage(),
                    exception
            );
        }
    }
}
