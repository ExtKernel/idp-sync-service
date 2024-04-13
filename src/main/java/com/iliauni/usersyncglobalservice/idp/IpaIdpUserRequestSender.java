package com.iliauni.usersyncglobalservice.idp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class IpaIdpUserRequestSender<T extends IpaClient> implements IdpUserRequestSender<T> {
    private final IdpRequestBuilder<T> requestBuilder;
    private final IdpObjectMapper idpObjectMapper;
    private final RestTemplate restTemplate;

    @Value("${ipaHostname}")
    private String ipaHostname;

    @Value("${ipaApiEndpoint}")
    private String ipaApiEndpoint;

    @Autowired
    public IpaIdpUserRequestSender(
            @Qualifier("ipaIdpRequestBuilder") IdpRequestBuilder<T> requestBuilder,
            @Qualifier("ipaIdpObjectMapper") IdpObjectMapper objectMapper,
            RestTemplateBuilder restTemplateBuilder) {
        this.requestBuilder = requestBuilder;
        this.idpObjectMapper = objectMapper;
        this.restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
    }

    @Override
    public User createUser(T client, User user) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("method", "user_add");
        requestBody.add("params", new Object[]{"", idpObjectMapper.mapUserToMap(user)});

        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(client.getId(), requestBody),
                Map.class);

        return user;
    }

    @Override
    public User getUser(T client, String username) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("method", "user_show");
        requestBody.add("params", new Object[]{username, new HashMap<>()});

        String userJson = restTemplate.exchange(
                        requestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                        HttpMethod.POST,
                        requestBuilder.buildHttpRequestEntity(client.getId(), requestBody),
                        String.class).getBody();

        return idpObjectMapper.mapUserMapToUser(mapIpaUsersMethodOutputToMap(userJson).get(0));
    }

    @Override
    public List<User> getUsers(T client) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("method", "user_find");
        requestBody.add("params", new Object[]{"", new HashMap<>()});

        String usersJson = restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(client.getId(), requestBody),
                String.class).getBody();

        List<MultiValueMap<String, Object>> userMaps = mapIpaUsersMethodOutputToMap(usersJson);
        List<User> users = new ArrayList<>();

        for (MultiValueMap<String, Object> userMap : userMaps) {
            users.add(idpObjectMapper.mapUserMapToUser(userMap));
        }

        return users;
    }

    private List<MultiValueMap<String, Object>> mapIpaUsersMethodOutputToMap(String usersJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(usersJson);
            JsonNode resultsNode = rootNode.path("result").path("result");

            MultiValueMap<String, Object> userMap = new LinkedMultiValueMap<>();
            List<MultiValueMap<String, Object>> userMaps = new ArrayList<>();

            for (JsonNode node : resultsNode) {
                userMap.add("uid", node.path("uid").get(0).asText());
                userMap.add("givenname", node.path("givenname").get(0).asText());
                userMap.add("sn", node.path("sn").get(0).asText());
                userMap.add("mail", node.path("mail").get(0).asText());

                userMaps.add(userMap);
            }

            return userMaps;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); // ???
        }
    }

    @Override
    public MultiValueMap<String, Object> updateUserPassword(T client, String username, String newPassword) {
        MultiValueMap<String, Object> credentials = idpObjectMapper.buildUserCredentialsMap(newPassword);

        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("method", "passwd");
        requestBody.add("params", new Object[]{username, credentials});

        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(client.getId(), requestBody),
                String.class).getBody();

        return credentials;
    }

    @Override
    public void deleteUser(T client, String username) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("method", "user_del");
        requestBody.add("params", new Object[]{username, new HashMap<>()});

        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(client.getId(), requestBody),
                String.class).getBody();
    }
}
