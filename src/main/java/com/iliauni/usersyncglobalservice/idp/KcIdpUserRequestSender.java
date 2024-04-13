package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.exception.KcUserWithUsernameNotFoundException;
import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.model.KcClient;
import com.iliauni.usersyncglobalservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class KcIdpUserRequestSender<T extends KcClient> implements IdpUserRequestSender<T> {
    private final IdpRequestBuilder<T> requestBuilder;
    private final IdpObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${kcRealm}")
    private String kcRealm;

    @Value("${KC_BASE_URL}")
    private String kcBaseUrl;

    @Autowired
    public KcIdpUserRequestSender(
            @Qualifier("kcIdpRequestBuilder") IdpRequestBuilder<T> requestBuilder,
            @Qualifier("kcIdpObjectMapper") IdpObjectMapper objectMapper,
            RestTemplateBuilder restTemplateBuilder) {
        this.requestBuilder = requestBuilder;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
    }

    @Override
    public User createUser(T client, User user) {
        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/users",
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(client.getId(), objectMapper.mapUserToMap(user)),
                Map.class);

        return user;
    }

    @Override
    public User getUser(T client, String username) {
        return objectMapper.mapUserMapToUser((MultiValueMap<String, Object>)
                restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/users/" + getUserId(client, username),
                HttpMethod.GET,
                requestBuilder.buildHttpRequestEntity(client.getId(), null),
                Map.class).getBody());
    }

    @Override
    public List<User> getUsers(T client) {
        List<MultiValueMap<String, Object>> userMaps = getUserMaps(client);
        List<User> users = new ArrayList<>();

        assert userMaps != null : "KC users are null";
        for (MultiValueMap<String, Object> user : userMaps) {
            users.add(objectMapper.mapUserMapToUser(user));
        }

        return users;
    }

    private String getUserId(T client, String username) {
        List<MultiValueMap<String, Object>> userMaps = getUserMaps(client);

        for (MultiValueMap<String, Object> user : userMaps) {
            if (Objects.equals(user.get("username").toString(), username)) {
                return user.get("id").toString();
            } else {
                throw new KcUserWithUsernameNotFoundException("KC user with username " + username + " was not found");
            }
        }

        return null;
    }

    private List<MultiValueMap<String, Object>> getUserMaps(T client) {
        return restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/users",
                HttpMethod.GET,
                requestBuilder.buildHttpRequestEntity(client.getId(), null),
                List.class).getBody();
    }

    @Override
    public MultiValueMap<String, Object> updateUserPassword(T client, String username, String newPassword) {
        MultiValueMap<String, Object> credentials = objectMapper.buildUserCredentialsMap(newPassword);

        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/users/" + getUserId(client, username) + "/reset-password",
                HttpMethod.PUT,
                requestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        credentials),
                Map.class).getBody();

        return credentials;
    }

    @Override
    public void deleteUser(T client, String username) {
        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/users/" + getUserId(client, username),
                HttpMethod.DELETE,
                requestBuilder.buildHttpRequestEntity(client.getId(), null),
                Map.class).getBody();
    }
}
