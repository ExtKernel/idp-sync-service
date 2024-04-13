package com.iliauni.usersyncglobalservice.unit.idp;

import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.idp.KcIdpObjectMapper;
import com.iliauni.usersyncglobalservice.idp.KcIdpRequestBuilder;
import com.iliauni.usersyncglobalservice.idp.KcIdpUserRequestSender;
import com.iliauni.usersyncglobalservice.model.KcClient;
import com.iliauni.usersyncglobalservice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KcIdpUserRequestSenderTest {
    @Mock
    RestTemplate restTemplate;

    @Mock
    KcIdpRequestBuilder<KcClient> kcIdpRequestBuilder;

    @Mock
    KcIdpObjectMapper kcIdpObjectMapper;

    @Mock
    RestTemplateBuilder restTemplateBuilder;

    KcIdpUserRequestSender<KcClient> kcIdpUserRequestSender;
    private final String kcBaseUrl = "test-base-url";
    private final String kcRealm = "test-realm";

    @BeforeEach
    public void setUpRestTemplateBuilder() {
        when(restTemplateBuilder.errorHandler(Mockito.any())).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.errorHandler(any(RestTemplateResponseErrorHandler.class)).build()).thenReturn(restTemplate);

        kcIdpUserRequestSender = new KcIdpUserRequestSender<>(
                kcIdpRequestBuilder,
                kcIdpObjectMapper,
                restTemplateBuilder
        );
    }

    @Test
    public void createUsergroup_WhenGivenClientAndUsergroup_ShouldReturnUsergroup()
            throws Exception {
        KcClient client = new KcClient();
        client.setId("test-id");

        User user = buildUserObject();
        MultiValueMap<String, Object> userMap = buildUserMap(user);

        when(kcIdpObjectMapper.mapUserToMap(user)).thenReturn(userMap);
        when(restTemplate.exchange(
                kcIdpRequestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/users",
                HttpMethod.POST,
                kcIdpRequestBuilder.buildHttpRequestEntity(client.getId(), userMap),
                Map.class
        )).thenReturn(null);

        assertEquals(user, kcIdpUserRequestSender.createUser(client, user));
    }

    @Test
    public void getUser_WhenGivenClientAndUsername_ShouldReturnUser()
            throws Exception {
        KcClient client = new KcClient();
        client.setId("test-id");

        User user = buildUserObject();
        MultiValueMap<String, Object> userMap = buildUserMap(user);

        List<MultiValueMap<String, Object>> userMaps = new ArrayList<>();
        userMaps.add(userMap);

        when(kcIdpObjectMapper.mapUserMapToUser(userMap)).thenReturn(user);
        when(restTemplate.exchange(
                kcIdpRequestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/users",
                HttpMethod.GET,
                kcIdpRequestBuilder.buildHttpRequestEntity(client.getId(), null),
                List.class
        )).thenReturn(new ResponseEntity<>(userMaps, HttpStatus.OK));
        when(restTemplate.exchange(
                kcIdpRequestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/users/" + userMap.get("id").get(0),
                HttpMethod.GET,
                kcIdpRequestBuilder.buildHttpRequestEntity(client.getId(), userMap),
                Map.class
        )).thenReturn(new ResponseEntity<>(userMap, HttpStatus.OK));

        assertEquals(user, kcIdpUserRequestSender.getUser(client, user.getUsername()));
    }

    @Test
    public void getUsers_WhenGivenClient_ShouldReturnUsersList()
            throws Exception {
        KcClient client = new KcClient();
        client.setId("test-id");

        User user = buildUserObject();
        MultiValueMap<String, Object> userMap = buildUserMap(user);

        List<MultiValueMap<String, Object>> userMaps = new ArrayList<>();
        userMaps.add(userMap);

        when(kcIdpObjectMapper.mapUserMapToUser(userMap)).thenReturn(user);
        when(restTemplate.exchange(
                kcIdpRequestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/users",
                HttpMethod.GET,
                kcIdpRequestBuilder.buildHttpRequestEntity(client.getId(), null),
                List.class
        )).thenReturn(new ResponseEntity<>(userMaps, HttpStatus.OK));

        assertEquals(user.getUsername(), kcIdpUserRequestSender.getUsers(client).get(0).getUsername());
    }

    private User buildUserObject() {
        User user = new User();
        user.setUsername("test-username");
        user.setFirstname("test-firstname");
        user.setLastname("test-lastname");
        user.setPassword("test-password");
        user.setEmail("test-email@test.com");

        return user;
    }

    private MultiValueMap<String, Object> buildUserMap(User user) {
        MultiValueMap<String, Object> userMap = new LinkedMultiValueMap<>();
        userMap.add("id", "test-id");
        userMap.add("username", user.getUsername());
        userMap.add("firstName", user.getFirstname());
        userMap.add("lastName", user.getLastname());
        userMap.add("email", user.getEmail());
        userMap.add("credentials", buildUserCredentialsMap(user.getPassword()));

        return userMap;
    }

    private MultiValueMap<String, Object> buildUserCredentialsMap(String password) {
        MultiValueMap<String, Object> userCredentials = new LinkedMultiValueMap<>();
        userCredentials.add("value", password);
        userCredentials.add("temporary", false);

        return userCredentials;
    }
}
