package com.iliauni.usersyncglobalservice.idp.win;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.idp.IdpObjectMapper;
import com.iliauni.usersyncglobalservice.idp.IdpRequestBuilder;
import com.iliauni.usersyncglobalservice.idp.IdpUsergroupRequestSender;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import com.iliauni.usersyncglobalservice.model.WinClient;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class WinIdpUsergroupRequestSender implements IdpUsergroupRequestSender<WinClient> {
    private final IdpRequestBuilder requestBuilder;
    private final IdpObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private String tokenUrl = null;

    @Value("${KC_REALM}")
    private String kcRealm;

    @Value("${KC_BASE_URL}")
    private String kcBaseUrl;

    @Autowired
    public WinIdpUsergroupRequestSender(
            @Qualifier("winIdpRequestBuilder") IdpRequestBuilder requestBuilder,
            @Qualifier("winIdpObjectMapper") IdpObjectMapper objectMapper,
            RestTemplateBuilder restTemplateBuilder
    ) {
        this.requestBuilder = requestBuilder;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
    }

    @PostConstruct
    private void setUpTokenUrl() {
        this.tokenUrl = "http://" + kcBaseUrl + "/realms/" + kcRealm + "/protocol/openid-connect/token";
    }

    @Override
    public Usergroup createUsergroup(
            WinClient client,
            Usergroup usergroup
    ) {
        String winBaseUrl;

        if (client.getFqdn() != null) {
            winBaseUrl = getWinHostname(client);
        } else {
            winBaseUrl = getWinIpWithPort(client);
        }

        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(winBaseUrl, "/groups/create/"),
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        objectMapper.mapUsergroupToMap(usergroup),
                        tokenUrl
                ),
                Map.class
        );

        return usergroup;
    }

    @Override
    public Usergroup getUsergroup(
            WinClient client,
            String usergroupName
    ) {
        String winBaseUrl;

        if (client.getFqdn() != null) {
            winBaseUrl = getWinHostname(client);
        } else {
            winBaseUrl = getWinIpWithPort(client);
        }

        return objectMapper.mapUsergroupMapToUsergroup((Map<String, Object>)
                restTemplate.exchange(
                        requestBuilder.buildApiBaseUrl(winBaseUrl, "/groups/" + usergroupName + "/"),
                        HttpMethod.GET,
                        requestBuilder.buildOnlyAuthHttpRequestEntity(
                                client.getId(),
                                tokenUrl
                        ),
                        Map.class
                ).getBody());
    }

    @Override
    public List<Usergroup> getUsergroups(WinClient client) {
        String winBaseUrl;

        if (client.getFqdn() != null) {
            winBaseUrl = getWinHostname(client);
        } else {
            winBaseUrl = getWinIpWithPort(client);
        }

        List<Map<String, Object>> usergroupMaps = restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(winBaseUrl, "/groups/"),
                HttpMethod.GET,
                requestBuilder.buildOnlyAuthHttpRequestEntity(
                        client.getId(),
                        tokenUrl
                ),
                List.class
        ).getBody();

        List<Usergroup> usergroups = new ArrayList<>();
        for (Map<String, Object> usergroupMap : usergroupMaps) {
            usergroups.add(objectMapper.mapUsergroupMapToUsergroup(usergroupMap));
        }

        return usergroups;
    }

    @Override
    public void deleteUsergroup(
            WinClient client,
            String usergroupName
    ) {
        String winBaseUrl;

        if (client.getFqdn() != null) {
            winBaseUrl = getWinHostname(client);
        } else {
            winBaseUrl = getWinIpWithPort(client);
        }

        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(winBaseUrl, "/groups/delete/") + usergroupName,
                HttpMethod.DELETE,
                requestBuilder.buildOnlyAuthHttpRequestEntity(
                        client.getId(),
                        tokenUrl
                ),
                Map.class
        );
    }

    private String getWinHostname(WinClient client) {
        // get a string until the first dot, which is, basically, a hostname
        return client.getFqdn().split("\\.")[0];
    }

    private String getWinIpWithPort(WinClient client) {
        return client.getIp() + ":" + client.getPort();
    }
}
