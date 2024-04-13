package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.exception.KcUserWithUsernameNotFoundException;
import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.model.KcClient;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
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
public class KcIdpUsergroupRequestSender<T extends KcClient> implements IdpUsergroupRequestSender<T> {
    private final IdpRequestBuilder<T> requestBuilder;
    private final IdpObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${kcRealm}")
    private String kcRealm;

    @Value("${KC_BASE_URL}")
    private String kcBaseUrl;

    @Autowired
    public KcIdpUsergroupRequestSender(
            @Qualifier("kcIdpRequestBuilder") IdpRequestBuilder<T> requestBuilder,
            @Qualifier("kcIdpObjectMapper") IdpObjectMapper objectMapper,
            RestTemplateBuilder restTemplateBuilder) {
        this.requestBuilder = requestBuilder;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
    }

    @Override
    public Usergroup createUsergroup(T client, Usergroup usergroup) {
        restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/groups",
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(client.getId(), objectMapper.mapUsergroupToMap(usergroup)),
                Map.class);

        return usergroup;
    }

    @Override
    public Usergroup getUsergroup(T client, String usergroupName) {
        return objectMapper.mapUsergroupMapToUsergroup((MultiValueMap<String, Object>)
                restTemplate.exchange(
                        requestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/groups/" + getUsergroupId(client, usergroupName),
                        HttpMethod.GET,
                        requestBuilder.buildHttpRequestEntity(client.getId(), null),
                        Map.class).getBody());
    }

    @Override
    public List<Usergroup> getUsergroups(T client) {
        List<MultiValueMap<String, Object>> usergroupMaps = getUsergroupMaps(client);
        List<Usergroup> usergroups = new ArrayList<>();

        assert usergroupMaps != null : "KC user groups are null";
        for (MultiValueMap<String, Object> usergroup : usergroupMaps) {
            usergroups.add(objectMapper.mapUsergroupMapToUsergroup(usergroup));
        }

        return usergroups;
    }

    private String getUsergroupId(T client, String usergroupName) {
        List<MultiValueMap<String, Object>> usergroupMaps = getUsergroupMaps(client);

        for (MultiValueMap<String, Object> usergroup : usergroupMaps) {
            if (Objects.equals(usergroup.get("name").get(0), usergroupName)) {
                return usergroup.get("id").get(0).toString();
            } else {
                throw new KcUserWithUsernameNotFoundException("KC user group with name " + usergroupName + " was not found");
            }
        }

        return null;
    }

    private List<MultiValueMap<String, Object>> getUsergroupMaps(T client) {
        return restTemplate.exchange(
                requestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/groups",
                HttpMethod.GET,
                requestBuilder.buildHttpRequestEntity(client.getId(), null),
                List.class).getBody();
    }

    @Override
    public void deleteUsergroup(T client, String usergroup_name) {

    }
}
