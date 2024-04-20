package com.iliauni.usersyncglobalservice.idp.win;

import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.idp.IdpObjectMapper;
import com.iliauni.usersyncglobalservice.idp.IdpRequestBuilder;
import com.iliauni.usersyncglobalservice.idp.IdpUserRequestSender;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.WinClient;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WinIdpUserRequestSender implements IdpUserRequestSender<WinClient> {
    private final IdpRequestBuilder requestBuilder;
    private final IdpObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private String tokenUrl = null;

    @Value("${KC_REALM}")
    private String kcRealm;

    @Value("${KC_BASE_URL}")
    private String kcBaseUrl;

    @Autowired
    public WinIdpUserRequestSender(
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
    public User createUser(
            WinClient client,
            User user
    ) {
        String winBaseUrl;

        if (client.getFqdn() != null) {
            winBaseUrl = getWinHostname(client);
        } else {
            winBaseUrl = getWinIpWithPort(client);
        }

        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(winBaseUrl, "/users/create/"),
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        objectMapper.mapUserToMap(user),
                        tokenUrl
                ),
                Map.class
        );

        return user;
    }

    @Override
    public User getUser(
            WinClient client,
            String username
    ) {
        String winBaseUrl;

        if (client.getFqdn() != null) {
            winBaseUrl = getWinHostname(client);
        } else {
            winBaseUrl = getWinIpWithPort(client);
        }

        return objectMapper.mapUserMapToUser((Map<String, Object>)
                restTemplate.exchange(
                        requestBuilder.buildApiBaseUrl(winBaseUrl, "/users/" + username + "/"),
                        HttpMethod.GET,
                        requestBuilder.buildOnlyAuthHttpRequestEntity(
                                client.getId(),
                                tokenUrl
                        ),
                        Map.class
                ).getBody());
    }

    @Override
    public List<User> getUsers(WinClient client) {
        String winBaseUrl;

        if (client.getFqdn() != null) {
            winBaseUrl = getWinHostname(client);
        } else {
            winBaseUrl = getWinIpWithPort(client);
        }

        List<Map<String, Object>> userMaps = restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(winBaseUrl, "/users/"),
                HttpMethod.GET,
                requestBuilder.buildOnlyAuthHttpRequestEntity(
                        client.getId(),
                        tokenUrl
                ),
                List.class
        ).getBody();

        List<User> users = new ArrayList<>();
        for (Map<String, Object> userMap : userMaps) {
            users.add(objectMapper.mapUserMapToUser(userMap));
        }

        return users;
    }

    @Override
    public Map<String, Object> updateUserPassword(
            WinClient client,
            String username,
            String newPassword
    ) {
        String winBaseUrl;

        if (client.getFqdn() != null) {
            winBaseUrl = getWinHostname(client);
        } else {
            winBaseUrl = getWinIpWithPort(client);
        }

        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(winBaseUrl, "/users/update-password/" + username),
                HttpMethod.PUT,
                requestBuilder.buildOnlyAuthHttpRequestEntity(
                        client.getId(),
                        tokenUrl
                ),
                Map.class
        );

        return objectMapper.buildUserCredentialsMap(newPassword);
    }

    @Override
    public void deleteUser(
            WinClient client,
            String username
    ) {
        String winBaseUrl;

        if (client.getFqdn() != null) {
            winBaseUrl = getWinHostname(client);
        } else {
            winBaseUrl = getWinIpWithPort(client);
        }

        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(winBaseUrl, "/users/delete/" + username),
                HttpMethod.PUT,
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
