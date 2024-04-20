package com.iliauni.usersyncglobalservice.idp.kc;

import com.iliauni.usersyncglobalservice.exception.KcUserWithUsernameNotFoundException;
import com.iliauni.usersyncglobalservice.exception.KcUsergroupWithNameNotFoundException;
import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.idp.IdpObjectMapper;
import com.iliauni.usersyncglobalservice.idp.IdpRequestBuilder;
import com.iliauni.usersyncglobalservice.idp.IdpUsergroupRequestSender;
import com.iliauni.usersyncglobalservice.model.KcClient;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import jakarta.annotation.PostConstruct;
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
 * A component class implementing the {@link IdpUsergroupRequestSender} interface for sending requests related to user groups in an Identity Provider (IDP) context specific to Keycloak (KC) systems.
 *
 * @param <T> the type of Keycloak client used for the request
 */
@Component
public class KcIdpUsergroupRequestSender<T extends KcClient> implements IdpUsergroupRequestSender<T> {
    private final IdpRequestBuilder requestBuilder;
    private final IdpObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private String tokenUrl = null;

    @Value("${KC_REALM}")
    private String kcRealm;

    @Value("${KC_BASE_URL}")
    private String kcBaseUrl;

    /**
     * Constructs a {@code KcIdpUsergroupRequestSender} instance with the specified {@link IdpRequestBuilder}, {@link IdpObjectMapper}, and {@link RestTemplateBuilder}.
     *
     * @param requestBuilder the request builder for Keycloak IDP
     * @param objectMapper the object mapper for Keycloak IDP
     * @param restTemplateBuilder the builder for creating RestTemplate
     */
    @Autowired
    public KcIdpUsergroupRequestSender(
            @Qualifier("kcIdpRequestBuilder") IdpRequestBuilder requestBuilder,
            @Qualifier("kcIdpObjectMapper") IdpObjectMapper objectMapper,
            RestTemplateBuilder restTemplateBuilder) {
        this.requestBuilder = requestBuilder;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
    }

    @PostConstruct
    private void setUpTokenUrl() {
        this.tokenUrl = "http://" + kcBaseUrl + "/realms/master/protocol/openid-connect/token";
    }

    /**
     * @inheritDoc
     * Sends a request to create a user group using the Keycloak client and object mapper.
     */
    @Override
    public Usergroup createUsergroup(
            T client,
            Usergroup usergroup
    ) {
        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/groups",
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

    /**
     * @inheritDoc
     * Sends a request to retrieve a user group with the specified name using the Keycloak client and object mapper.
     */
    @Override
    public Usergroup getUsergroup(
            T client,
            String usergroupName
    ) {
        return objectMapper.mapUsergroupMapToUsergroup((Map<String, Object>)
                restTemplate.exchange(
                        requestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/groups/" + getUsergroupId(client, usergroupName),
                        HttpMethod.GET,
                        requestBuilder.buildHttpRequestEntity(
                                client.getId(),
                                null,
                                tokenUrl
                        ),
                        Map.class
                ).getBody());
    }

    /**
     * @inheritDoc
     * Sends a request to retrieve all user groups using the Keycloak client and object mapper.
     */
    @Override
    public List<Usergroup> getUsergroups(T client) {
        List<Map<String, Object>> usergroupMaps = getUsergroupMaps(client);
        List<Usergroup> usergroups = new ArrayList<>();

        assert usergroupMaps != null : "KC user groups are null";
        for (Map<String, Object> usergroup : usergroupMaps) {
            usergroups.add(objectMapper.mapUsergroupMapToUsergroup(usergroup));
        }

        return usergroups;
    }

    /**
     * @inheritDoc
     * Sends a request to delete a user group with the specified name using the Keycloak client.
     */
    @Override
    public void deleteUsergroup(
            T client,
            String usergroupName
    ) {
        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/groups/" + getUsergroupId(client, usergroupName),
                HttpMethod.DELETE,
                requestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        null,
                        tokenUrl
                ),
                Map.class
        );
    }

    /**
     * Retrieves the ID of the user group with the specified name using the Keycloak client.
     *
     * @param client the Keycloak client
     * @param usergroupName the name of the user group
     * @return the ID of the user group
     * @throws KcUserWithUsernameNotFoundException if the user group with the specified name is not found
     */
    private String getUsergroupId(
            T client,
            String usergroupName
    ) {
        List<Map<String, Object>> usergroupMaps = getUsergroupMaps(client);
        Map<String, Object> usergroup1 = usergroupMaps.get(0);
        String c = usergroup1.get("name").toString();

        for (Map<String, Object> usergroup : usergroupMaps) {
            if (usergroup.get("name").toString().equals(usergroupName)) {
                return usergroup.get("id").toString();
            }
        }
        throw new KcUsergroupWithNameNotFoundException("KC user group with name " + usergroupName + " was not found");
    }

    /**
     * Retrieves a list of MultiValueMap representing user groups using the Keycloak client.
     *
     * @param client the Keycloak client
     * @return a list of MultiValueMap representing user groups
     */
    private List<Map<String, Object>> getUsergroupMaps(T client) {
        return restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/groups",
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
