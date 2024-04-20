package com.iliauni.usersyncglobalservice.idp.kc;

import com.iliauni.usersyncglobalservice.exception.KcUserWithUsernameNotFoundException;
import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.idp.IdpObjectMapper;
import com.iliauni.usersyncglobalservice.idp.IdpRequestBuilder;
import com.iliauni.usersyncglobalservice.idp.IdpUserRequestSender;
import com.iliauni.usersyncglobalservice.model.KcClient;
import com.iliauni.usersyncglobalservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A component class implementing the {@link IdpUserRequestSender} interface for sending user-related requests in an Identity Provider (IDP) context specific to Keycloak (KC) systems.
 *
 * @param <T> the type of KC client used for the request
 */
@Component
public class KcIdpUserRequestSender<T extends KcClient> implements IdpUserRequestSender<T> {
    private final IdpRequestBuilder requestBuilder;
    private final IdpObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final String tokenUrl;

    @Value("${KC_REALM}")
    private String kcRealm;

    @Value("${KC_BASE_URL}")
    private String kcBaseUrl;

    /**
     * Constructs an {@code KcIdpUserRequestSender} instance with the specified {@link IdpRequestBuilder}, {@link IdpObjectMapper}, and {@link RestTemplateBuilder}.
     *
     * @param requestBuilder the request builder for KC IDP
     * @param objectMapper the object mapper for KC IDP
     * @param restTemplateBuilder the builder for creating RestTemplate
     */
    @Autowired
    public KcIdpUserRequestSender(
            @Qualifier("kcIdpRequestBuilder") IdpRequestBuilder requestBuilder,
            @Qualifier("kcIdpObjectMapper") IdpObjectMapper objectMapper,
            RestTemplateBuilder restTemplateBuilder
    ) {
        this.requestBuilder = requestBuilder;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
        this.tokenUrl = requestBuilder.buildApiBaseUrl(kcBaseUrl, "master") + "/protocol/openid-connect/token";
    }

    /**
     * @inheritDoc
     * Sends a request to create a user using the KC client and object mapper.
     */
    @Override
    public User createUser(
            T client,
            User user
    ) {
        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/users",
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        objectMapper.mapUserToMap(user),
                        tokenUrl
                ),
                Map.class);

        return user;
    }

    /**
     * @inheritDoc
     * Sends a request to retrieve a user with the specified username using the KC client and object mapper.
     */
    @Override
    public User getUser(
            T client,
            String username
    ) {
        return objectMapper.mapUserMapToUser((Map<String, Object>)
                restTemplate.exchange(
                        requestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/users/" + getUserId(client, username),
                    HttpMethod.GET,
                        requestBuilder.buildOnlyAuthHttpRequestEntity(
                                client.getId(),
                                tokenUrl
                        ),
                    Map.class
                ).getBody());
    }

    /**
     * @inheritDoc
     * Sends a request to retrieve all users using the KC client and object mapper.
     */
    @Override
    public List<User> getUsers(T client) {
        List<Map<String, Object>> userMaps = getUserMaps(client);
        List<User> users = new ArrayList<>();

        assert userMaps != null : "KC users are null";
        for (Map<String, Object> user : userMaps) {
            users.add(objectMapper.mapUserMapToUser(user));
        }

        return users;
    }

    /**
     * @inheritDoc
     * Sends a request to update the password of a user with the specified username using the KC client and object mapper.
     */
    @Override
    public Map<String, Object> updateUserPassword(
            T client,
            String username,
            String newPassword
    ) {
        Map<String, Object> credentials = objectMapper.buildUserCredentialsMap(newPassword);

        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/users/" + getUserId(client, username) + "/reset-password",
                HttpMethod.PUT,
                requestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        credentials,
                        tokenUrl
                ),
                Map.class
        ).getBody();

        return credentials;
    }

    /**
     * @inheritDoc
     * Sends a request to delete a user with the specified username using the KC client.
     */
    @Override
    public void deleteUser(
            T client,
            String username
    ) {
        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/users/" + getUserId(client, username),
                HttpMethod.DELETE,
                requestBuilder.buildOnlyAuthHttpRequestEntity(
                        client.getId(),
                        tokenUrl
                ),
                Map.class
        ).getBody();
    }

    /**
     * Retrieves the user ID for the given username using the KC client.
     * Throws an exception if the user with the specified username is not found.
     *
     * @param client the KC client
     * @param username the username of the user
     * @return the user ID
     * @throws KcUserWithUsernameNotFoundException if the user with the specified username is not found
     */
    private String getUserId(
            T client,
            String username
    ) {
        List<Map<String, Object>> userMaps = getUserMaps(client);

        for (Map<String, Object> user : userMaps) {
            if (Objects.equals(user.get("username").toString(), username)) {
                return user.get("id").toString();
            }
        }
        throw new KcUserWithUsernameNotFoundException("KC user with username " + username + " was not found");
    }

    /**
     * Retrieves a list of user maps using the KC client.
     *
     * @param client the KC client
     * @return a list of user maps
     */
    private List<Map<String, Object>> getUserMaps(T client) {
        return restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/users",
                HttpMethod.GET,
                requestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        null,
                        tokenUrl
                ),
                List.class
        ).getBody();
    }
}
