package com.iliauni.usersyncglobalservice.idp.ipa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.idp.IdpObjectMapper;
import com.iliauni.usersyncglobalservice.idp.IdpRequestBuilder;
import com.iliauni.usersyncglobalservice.idp.IdpUsergroupRequestSender;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A component class implementing the {@link IdpUsergroupRequestSender} interface for sending requests related to user groups in an Identity Provider (IDP) context specific to FreeIPA (Identity, Policy, and Audit) systems.
 */
@Component
public class IpaIdpUsergroupRequestSender implements IdpUsergroupRequestSender<IpaClient> {
    private final IdpRequestBuilder requestBuilder;
    private final IdpObjectMapper idpObjectMapper;
    private final RestTemplate restTemplate;

    @Value("${ipaApiEndpoint}")
    private String ipaApiEndpoint;

    @Value("${ipaApiAuthEndpoint}")
    private String ipaAuthEndpoint;

    /**
     * Constructs an {@code IpaIdpUsergroupRequestSender} instance with the specified {@link IdpRequestBuilder}, {@link IdpObjectMapper}, and {@link RestTemplateBuilder}.
     *
     * @param requestBuilder   the request builder for IPA IDP
     * @param objectMapper     the object mapper for IPA IDP
     * @param restTemplateBuilder the builder for creating RestTemplate
     */
    @Autowired
    public IpaIdpUsergroupRequestSender(
            @Qualifier("ipaIdpRequestBuilder") IdpRequestBuilder requestBuilder,
            @Qualifier("ipaIdpObjectMapper") IdpObjectMapper objectMapper,
            RestTemplateBuilder restTemplateBuilder
    ) {
        this.requestBuilder = requestBuilder;
        this.idpObjectMapper = objectMapper;
        this.restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
    }

    /**
     * @inheritDoc
     * Sends a request to create a user group using the IPA client and object mapper.
     */
    @Override
    public Usergroup createUsergroup(
            IpaClient client,
            Usergroup usergroup
    ) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "group_add");
        requestBody.put("params", new Object[]{"", idpObjectMapper.mapUsergroupToMap(usergroup)});

        String ipaHostname = getIpaHostname(client);
        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        requestBody,
                        buildAuthUrl(ipaHostname)
                ),
                Map.class
        );

        return usergroup;
    }

    /**
     * @inheritDoc
     * Sends a request to retrieve a user group with the specified name using the IPA client and object mapper.
     */
    @Override
    public Usergroup getUsergroup(
            IpaClient client,
            String usergroupName
    ) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "group_show");
        requestBody.put("params", new Object[]{usergroupName, new HashMap<>()});

        String ipaHostname = getIpaHostname(client);
        String usergroupJson = restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        requestBody,
                        buildAuthUrl(ipaHostname)
                ),
                String.class
        ).getBody();

        return idpObjectMapper.mapUsergroupMapToUsergroup(mapIpaUsergroupShowMethodOutputToMap(usergroupJson));
    }

    /**
     * @inheritDoc
     * Sends a request to retrieve all user groups using the IPA client and object mapper.
     */
    @Override
    public List<Usergroup> getUsergroups(IpaClient client) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "group_find");
        requestBody.put("params", new Object[]{"", new HashMap<>()});

        String ipaHostname = getIpaHostname(client);
        String usersJson = restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        requestBody,
                        buildAuthUrl(ipaHostname)
                ),
                String.class
        ).getBody();

        List<Map<String, Object>> usergroupMaps = mapIpaUsergroupFindMethodOutputToMap(usersJson);
        List<Usergroup> usergroups = new ArrayList<>();

        for (Map<String, Object> userMap : usergroupMaps) {
            usergroups.add(idpObjectMapper.mapUsergroupMapToUsergroup(userMap));
        }

        return usergroups;
    }

    /**
     * @inheritDoc
     * Sends a request to delete a user group with the specified name using the IPA client.
     */
    @Override
    public void deleteUsergroup(
            IpaClient client,
            String usergroupName
    ) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "group_del");
        requestBody.put("params", new Object[]{usergroupName, new HashMap<>()});

        String ipaHostname = getIpaHostname(client);
        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        requestBody,
                        buildAuthUrl(ipaHostname)
                ),
                String.class
        ).getBody();
    }

    private String getIpaHostname(IpaClient client) {
        // get a string until the first dot, which is, basically, a hostname
        return client.getFqdn().split("\\.")[0];
    }

    private String buildAuthUrl(String ipaHostname) {
        return requestBuilder.buildApiBaseUrl(ipaHostname, ipaAuthEndpoint);
    }

    /**
     * Maps the JSON response of the "group_show" method to a {@link Map}.
     *
     * @param usergroupsJson the JSON response of the "group_show" method
     * @return the user group data mapped to a MultiValueMap
     */
    private Map<String, Object> mapIpaUsergroupShowMethodOutputToMap(String usergroupsJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(usergroupsJson);
            JsonNode resultsNode = rootNode.path("result").path("result");

            Map<String, Object> usergroupMap = new HashMap<>();
            usergroupMap.put("cn", resultsNode.path("cn").get(0).asText());
            usergroupMap.put("description", resultsNode.path("description").get(0).asText());

            return usergroupMap;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); // ???
        }
    }

    /**
     * Maps the JSON response of the "group_find" method to a list of {@link Map}.
     *
     * @param usergroupsJson the JSON response of the "group_find" method
     * @return the user group data mapped to a list of MultiValueMap
     */
    private List<Map<String, Object>> mapIpaUsergroupFindMethodOutputToMap(String usergroupsJson) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(usergroupsJson);
            JsonNode resultNode = rootNode.path("result").path("result");

            List<Map<String, Object>> usergroupMapList = new ArrayList<>();

            for (JsonNode node : resultNode) {
                Map<String, Object> usergroupMap = new HashMap<>();

                usergroupMap.put("cn", node.path("cn").get(0).asText());

                if (!node.path("description").isEmpty()) {
                    usergroupMap.put("description", node.path("description").get(0).asText());
                }

                usergroupMapList.add(usergroupMap);
            }

            return usergroupMapList;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); // ???
        }
    }
}
