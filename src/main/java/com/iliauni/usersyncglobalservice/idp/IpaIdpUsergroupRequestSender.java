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
    public Usergroup getUsergroup(T client, String usergroup_name) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("method", "group_show");
        requestBody.add("params", new Object[]{usergroup_name, new HashMap<>()});

        String usergroupJson = restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(client.getId(), requestBody),
                String.class).getBody();

        return idpObjectMapper.mapUsergroupMapToUsergroup(mapIpaUsergroupsMethodOutputToMap(usergroupJson).get(0));
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

        List<MultiValueMap<String, Object>> usergroupMaps = mapIpaUsergroupsMethodOutputToMap(usersJson);
        List<Usergroup> usergroups = new ArrayList<>();

        for (MultiValueMap<String, Object> userMap : usergroupMaps) {
            usergroups.add(idpObjectMapper.mapUsergroupMapToUsergroup(userMap));
        }

        return usergroups;
    }

    private List<MultiValueMap<String, Object>> mapIpaUsergroupsMethodOutputToMap(String usergroupsJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(usergroupsJson);
            JsonNode resultsNode = rootNode.path("result").path("result");

            MultiValueMap<String, Object> usergroupMap = new LinkedMultiValueMap<>();
            List<MultiValueMap<String, Object>> usergroupMaps = new ArrayList<>();

            for (JsonNode node : resultsNode) {
                usergroupMap.add("uid", node.path("uid").get(0).asText());
                usergroupMap.add("givenname", node.path("givenname").get(0).asText());
                usergroupMap.add("sn", node.path("sn").get(0).asText());
                usergroupMap.add("mail", node.path("mail").get(0).asText());

                usergroupMaps.add(usergroupMap);
            }

            return usergroupMaps;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); // ???
        }
    }

    @Override
    public void deleteUsergroup(T client, String usergroup_name) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("method", "group_del");
        requestBody.add("params", new Object[]{usergroup_name, new HashMap<>()});

        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(client.getId(), requestBody),
                String.class).getBody();
    }
}
