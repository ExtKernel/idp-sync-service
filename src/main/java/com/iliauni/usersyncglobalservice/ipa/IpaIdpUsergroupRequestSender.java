package com.iliauni.usersyncglobalservice.ipa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.usersyncglobalservice.exception.UsergroupJsonReadingException;
import com.iliauni.usersyncglobalservice.exception.UsergroupMembersJsonReadingException;
import com.iliauni.usersyncglobalservice.exception.UsergroupsJsonReadingException;
import com.iliauni.usersyncglobalservice.exception.WritingRequestBodyToStringException;
import com.iliauni.usersyncglobalservice.idp.IdpJsonObjectMapper;
import com.iliauni.usersyncglobalservice.idp.IdpRequestBuilder;
import com.iliauni.usersyncglobalservice.idp.IdpUsergroupRequestSender;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * A component class implementing the {@link IdpUsergroupRequestSender} interface for sending requests
 * related to user groups in an Identity Provider (IDP) context
 * specific to FreeIPA (Identity, Policy, and Audit) systems.
 */
@Component
public class IpaIdpUsergroupRequestSender implements IdpUsergroupRequestSender<IpaClient> {
    private final IdpRequestBuilder<IpaClient> requestBuilder;
    private final IdpJsonObjectMapper jsonObjectMapper;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${ipaApiEndpoint}")
    private String ipaApiEndpoint;

    @Value("${ipaApiAuthEndpoint}")
    private String ipaAuthEndpoint;

    @Autowired
    public IpaIdpUsergroupRequestSender(
            IdpRequestBuilder<IpaClient> requestBuilder,
            @Qualifier("ipaIdpJsonObjectMapper") IdpJsonObjectMapper jsonObjectMapper,
            ObjectMapper objectMapper
    ) {
        this.requestBuilder = requestBuilder;
        this.jsonObjectMapper = jsonObjectMapper;
        this.restTemplate = requestBuilder.getRestTemplate();
        this.objectMapper = objectMapper;
    }

    @Override
    public Usergroup sendCreateUsergroupRequest(
            IpaClient client,
            Usergroup usergroup
    ) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "group_add");
        requestBody.put("params", new Object[]{
                usergroup.getName(),
                jsonObjectMapper.mapUsergroupToJsonString(usergroup)
        });

        sendRestTemplateRequest(client, requestBody);

        return usergroup;
    }

    @Override
    public void sendAddUsergroupMemberRequest(
            IpaClient client,
            String usergroupName,
            String username
    ) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "group_add_member");
        requestBody.put("params", new Object[]{
                usergroupName,
                new HashMap<>().put("user", username)
        });

        sendRestTemplateRequest(client, requestBody);
    }

    @Override
    public JsonNode sendGetUsergroupRequest(
            IpaClient client,
            String usergroupName
    ) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "group_show");
        requestBody.put("params", new Object[]{usergroupName, new HashMap<>()});

        try {
            return objectMapper.readTree(
                    sendRestTemplateRequest(
                            client,
                            requestBody
                    )
            );
        } catch (JsonProcessingException exception) {
            throw new UsergroupJsonReadingException(
                    "An exception occurred while reading JSON received from the request"
                            + " to retrieve a user group for FreeIPA client with id "
                            + client.getId() + ": "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    @Override
    public JsonNode sendGetUsergroupsRequest(IpaClient client) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "group_find");
        requestBody.put("params", new Object[]{"", new HashMap<>()});

        try {
            return objectMapper.readTree(
                    sendRestTemplateRequest(
                            client,
                            requestBody
                    )
            );
        } catch (JsonProcessingException exception) {
            throw new UsergroupsJsonReadingException(
                    "An exception occurred while reading JSON received from the request"
                            + " to retrieve user groups for FreeIPA client with id "
                            + client.getId() + ": "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    @Override
    public JsonNode sendGetUsergroupMembersRequest(
            IpaClient client,
            String usergroupName
    ) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "group_show");
        requestBody.put("params", new Object[]{usergroupName, new HashMap<>()});

        try {
            return objectMapper.readTree(
                    sendRestTemplateRequest(
                            client,
                            requestBody
                    )
            );
        } catch (JsonProcessingException exception) {
            throw new UsergroupMembersJsonReadingException(
                    "An exception occurred while reading JSON received from the request"
                            + " to retrieve user group members for FreeIPA client with id "
                            + client.getId() + ": "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    @Override
    public void sendDeleteUsergroupRequest(
            IpaClient client,
            String usergroupName
    ) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "group_del");
        requestBody.put("params", new Object[]{usergroupName, new HashMap<>()});

        sendRestTemplateRequest(client, requestBody);
    }

    @Override
    public void sendRemoveUsergroupMemberRequest(
            IpaClient client,
            String usergroupName,
            String username
    ) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "group_remove_member");
        requestBody.put("params", new Object[]{
                usergroupName,
                new HashMap<>().put("user", username)
        });

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
                            + exception.getMessage(),
                    exception
            );
        }
    }
}
