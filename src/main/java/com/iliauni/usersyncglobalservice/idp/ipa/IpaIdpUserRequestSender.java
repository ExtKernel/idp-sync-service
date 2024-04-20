package com.iliauni.usersyncglobalservice.idp.ipa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.idp.IdpObjectMapper;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A component class implementing the {@link IdpUserRequestSender} interface for sending user-related requests in an Identity Provider (IDP) context specific to IPA (Identity, Policy, and Audit) systems.
 */
@Component
public class IpaIdpUserRequestSender implements IdpUserRequestSender<IpaClient> {
    private final IdpRequestBuilder requestBuilder;
    private final IdpObjectMapper idpObjectMapper;
    private final RestTemplate restTemplate;

    @Value("${ipaApiEndpoint}")
    private String ipaApiEndpoint;

    @Value("${ipaApiAuthEndpoint}")
    private String ipaAuthEndpoint;

    /**
     * Constructs an {@code IpaIdpUserRequestSender} instance with the specified {@link IdpRequestBuilder}, {@link IdpObjectMapper}, and {@link RestTemplateBuilder}.
     *
     * @param requestBuilder the request builder for IPA IDP
     * @param objectMapper the object mapper for IPA IDP
     * @param restTemplateBuilder the builder for creating RestTemplate
     */
    @Autowired
    public IpaIdpUserRequestSender(
            @Qualifier("ipaIdpRequestBuilder") IdpRequestBuilder requestBuilder,
            @Qualifier("ipaIdpObjectMapper") IdpObjectMapper objectMapper,
            RestTemplateBuilder restTemplateBuilder) {
        this.requestBuilder = requestBuilder;
        this.idpObjectMapper = objectMapper;
        this.restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
    }

    /**
     * @inheritDoc
     * Sends a request to create a user using the IPA client and object mapper.
     */
    @Override
    public User createUser(
            IpaClient client,
            User user
    ) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "user_add");
        requestBody.put("params", new Object[]{"", idpObjectMapper.mapUserToMap(user)});

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

        // call the update password method because there's no way to set password directly in FreeIPA while user creation
        updateUserPassword(
                client,
                user.getUsername(),
                user.getPassword()
        );

        return user;
    }

    /**
     * @inheritDoc
     * Sends a request to retrieve a user with the specified username using the IPA client and object mapper.
     */
    @Override
    public User getUser(IpaClient client, String username) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "user_show");
        requestBody.put("params", new Object[]{username, new HashMap<>()});

        String ipaHostname = getIpaHostname(client);
        String userJson = restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        requestBody,
                        buildAuthUrl(ipaHostname)
                ),
                String.class
        ).getBody();

        return idpObjectMapper.mapUserMapToUser(mapIpaUserShowMethodOutputToMap(userJson));
    }

    /**
     * @inheritDoc
     * Sends a request to retrieve all users using the IPA client and object mapper.
     */
    @Override
    public List<User> getUsers(IpaClient client) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "user_find");
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

        List<Map<String, Object>> userMaps = mapIpaUserFindMethodOutputToMap(usersJson);
        List<User> users = new ArrayList<>();

        for (Map<String, Object> userMap : userMaps) {
            users.add(idpObjectMapper.mapUserMapToUser(userMap));
        }

        return users;
    }

    /**
     * @inheritDoc
     * Sends a request to update the password of a user with the specified username using the IPA client and object mapper.
     */
    @Override
    public Map<String, Object> updateUserPassword(
            IpaClient client,
            String username,
            String newPassword
    ) {
        Map<String, Object> credentials = idpObjectMapper.buildUserCredentialsMap(newPassword);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "passwd");
        requestBody.put("params", new Object[]{username, credentials.get("value")});

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
        );

        return credentials;
    }

    /**
     * @inheritDoc
     * Sends a request to delete a user with the specified username using the IPA client.
     */
    @Override
    public void deleteUser(
            IpaClient client,
            String username
    ) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "user_del");
        requestBody.put("params", new Object[]{username, new HashMap<>()});

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
     * Maps the JSON response of the "user_show" method to a {@link Map}.
     *
     * @param usersJson the JSON response of the "user_show" method
     * @return the user data mapped to a MultiValueMap
     */
    private Map<String, Object> mapIpaUserShowMethodOutputToMap(String usersJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(usersJson);
            JsonNode resultsNode = rootNode.path("result").path("result");

            return mapJsonNodeToUserMap(resultsNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); // ???
        }
    }

    /**
     * Maps the JSON response of the "user_find" method to a list of {@link Map}.
     *
     * @param usersJson the JSON response of the "user_find" method
     * @return the user data mapped to a list of MultiValueMap
     */
    private List<Map<String, Object>> mapIpaUserFindMethodOutputToMap(String usersJson) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(usersJson);
            JsonNode resultNode = rootNode.path("result").path("result");

            List<Map<String, Object>> userMapList = new ArrayList<>();

            for (JsonNode node : resultNode) {
                Map<String, Object> userMap = mapJsonNodeToUserMap(node);
                userMapList.add(userMap);
            }

            return userMapList;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); // ???
        }
    }

    /**
     * Maps a JSON node containing FreeIPA user representation to a map
     *
     * @param node A JSON node containing FreeIPA user representation
     * @return A map with values from the node
     */
    private Map<String, Object> mapJsonNodeToUserMap(JsonNode node) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("uid", node.path("uid").get(0).asText());
        userMap.put("givenname", node.path("givenname").get(0).asText());
        userMap.put("sn", node.path("sn").get(0).asText());
        userMap.put("mail", node.path("mail").get(0).asText());

        return userMap;
    }
}
