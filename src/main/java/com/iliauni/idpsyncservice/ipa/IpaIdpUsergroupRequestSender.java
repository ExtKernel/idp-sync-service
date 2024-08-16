package com.iliauni.idpsyncservice.ipa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.idpsyncservice.exception.UsergroupJsonReadingException;
import com.iliauni.idpsyncservice.exception.UsergroupMembersJsonReadingException;
import com.iliauni.idpsyncservice.exception.UsergroupsJsonReadingException;
import com.iliauni.idpsyncservice.exception.WritingRequestBodyToStringException;
import com.iliauni.idpsyncservice.idp.IdpMapObjectMapper;
import com.iliauni.idpsyncservice.idp.IdpRequestBuilder;
import com.iliauni.idpsyncservice.idp.IdpUsergroupRequestSender;
import com.iliauni.idpsyncservice.model.IpaClient;
import com.iliauni.idpsyncservice.model.Usergroup;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

/**
 * A component class implementing the {@link IdpUsergroupRequestSender} interface for sending requests
 * related to user groups in an Identity Provider (IDP) context
 * specific to FreeIPA (Identity, Policy, and Audit) systems.
 */
@Component
public class IpaIdpUsergroupRequestSender implements IdpUsergroupRequestSender<IpaClient> {
    private final IdpRequestBuilder<IpaClient> requestBuilder;
    // a temporary fix, implemented because of specifics the FreeIPA's API
    // using IdpMapObjectMapper instead of IdpJsonObjectMapper
    // private final IdpJsonObjectMapper jsonObjectMapper;
    private final IdpMapObjectMapper mapObjectMapper;
    private final ObjectMapper objectMapper;

    @Value("${ipaApiEndpoint}")
    private String ipaApiEndpoint;

    @Value("${ipaApiAuthEndpoint}")
    private String ipaAuthEndpoint;

    @Autowired
    public IpaIdpUsergroupRequestSender(
            IdpRequestBuilder<IpaClient> requestBuilder,
//            @Qualifier("ipaIdpJsonObjectMapper") IdpJsonObjectMapper jsonObjectMapper,
            @Qualifier("ipaIdpMapObjectMapper") IdpMapObjectMapper mapObjectMapper,
            ObjectMapper objectMapper
    ) {
        this.requestBuilder = requestBuilder;
//        this.jsonObjectMapper = jsonObjectMapper;
        this.mapObjectMapper = mapObjectMapper;
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
                new Object[]{usergroup.getName()},
//                a part of the temporary fix. Map to a Hashmap instead
//                jsonObjectMapper.mapUsergroupToJsonString(usergroup)
                mapObjectMapper.mapUsergroupToMap(usergroup)
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

        Map<String, Object> options = new HashMap<>();
        options.put("user", username);

        requestBody.put("params", new Object[]{
                new Object[]{usergroupName},
                options
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
        requestBody.put("params", new Object[]{
                new Object[]{usergroupName},
                new HashMap<>()
        });

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

    @Cacheable("ipaUsergroups")
    @Override
    public JsonNode sendGetUsergroupsRequest(IpaClient client) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "group_find");
        requestBody.put("params", new Object[]{
                new Object[]{""},
                new HashMap<>()
        });

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
        requestBody.put("params", new Object[]{
                new Object[]{usergroupName},
                new HashMap<>()
        });

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
        requestBody.put("params", new Object[]{
                new Object[]{usergroupName},
                new HashMap<>()
        });

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

        Map<String, Object> options = new HashMap<>();
        options.put("user", username);

        requestBody.put("params", new Object[]{
                new Object[]{usergroupName},
                options
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
