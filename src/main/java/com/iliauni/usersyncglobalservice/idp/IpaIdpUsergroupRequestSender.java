package com.iliauni.usersyncglobalservice.idp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.model.Usergroup;
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
public class IpaIdpUsergroupRequestSender<T extends IpaClient> implements IdpUsergroupRequestSender<T> {
    private final IdpRequestBuilder<T> requestBuilder;
    private final IdpObjectMapper idpObjectMapper;
    private final RestTemplate restTemplate;

    @Value("${ipaHostname}")
    private String ipaHostname;

    @Value("${ipaApiEndpoint}")
    private String ipaApiEndpoint;

    @Autowired
    public IpaIdpUsergroupRequestSender(
            @Qualifier("ipaIdpRequestBuilder") IdpRequestBuilder<T> requestBuilder,
            @Qualifier("ipaIdpObjectMapper") IdpObjectMapper objectMapper,
            RestTemplateBuilder restTemplateBuilder) {
        this.requestBuilder = requestBuilder;
        this.idpObjectMapper = objectMapper;
        this.restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
    }

    @Override
    public Usergroup createUsergroup(T client, Usergroup usergroup) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("method", "group_add");
        requestBody.add("params", new Object[]{"", idpObjectMapper.mapUsergroupToMap(usergroup)});

        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(client.getId(), requestBody),
                Map.class);

        return usergroup;
    }

    @Override
    public Usergroup getUsergroup(T client, String usergroupName) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("method", "group_show");
        requestBody.add("params", new Object[]{usergroupName, new HashMap<>()});

        String usergroupJson = restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(client.getId(), requestBody),
                String.class).getBody();

        return idpObjectMapper.mapUsergroupMapToUsergroup(mapIpaUsergroupShowMethodOutputToMap(usergroupJson));
    }

    @Override
    public List<Usergroup> getUsergroups(T client) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("method", "group_find");
        requestBody.add("params", new Object[]{"", new HashMap<>()});

        String usersJson = restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(client.getId(), requestBody),
                String.class).getBody();

        List<MultiValueMap<String, Object>> usergroupMaps = mapIpaUsergroupFindMethodOutputToMap(usersJson);
        List<Usergroup> usergroups = new ArrayList<>();

        for (MultiValueMap<String, Object> userMap : usergroupMaps) {
            usergroups.add(idpObjectMapper.mapUsergroupMapToUsergroup(userMap));
        }

        return usergroups;
    }

    private MultiValueMap<String, Object> mapIpaUsergroupShowMethodOutputToMap(String usergroupsJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(usergroupsJson);
            JsonNode resultsNode = rootNode.path("result").path("result");

            MultiValueMap<String, Object> usergroupMap = new LinkedMultiValueMap<>();
            usergroupMap.add("cn", resultsNode.path("cn").get(0).asText());
            usergroupMap.add("description", resultsNode.path("description").get(0).asText());

            return usergroupMap;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); // ???
        }
    }

    private List<MultiValueMap<String, Object>> mapIpaUsergroupFindMethodOutputToMap(String usergroupsJson) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(usergroupsJson);
            JsonNode resultNode = rootNode.path("result").path("result");

            List<MultiValueMap<String, Object>> usergroupMapList = new ArrayList<>();

            for (JsonNode node : resultNode) {
                MultiValueMap<String, Object> usergroupMap = new LinkedMultiValueMap<>();

                usergroupMap.add("cn", node.path("cn").get(0).asText());

                if (!node.path("description").isEmpty()) {
                    usergroupMap.add("description", node.path("description").get(0).asText());
                }

                usergroupMapList.add(usergroupMap);
            }

            return usergroupMapList;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); // ???
        }
    }

    @Override
    public void deleteUsergroup(T client, String usergroupName) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("method", "group_del");
        requestBody.add("params", new Object[]{usergroupName, new HashMap<>()});

        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(client.getId(), requestBody),
                String.class).getBody();
    }
}
