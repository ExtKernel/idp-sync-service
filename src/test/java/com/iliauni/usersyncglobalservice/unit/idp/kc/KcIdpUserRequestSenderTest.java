package com.iliauni.usersyncglobalservice.unit.idp.kc;

import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.idp.kc.KcIdpObjectMapper;
import com.iliauni.usersyncglobalservice.idp.kc.KcIdpRequestBuilder;
import com.iliauni.usersyncglobalservice.idp.kc.KcIdpUserRequestSender;
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
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KcIdpUserRequestSenderTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private KcIdpRequestBuilder kcIdpRequestBuilder;

    @Mock
    private KcIdpObjectMapper kcIdpObjectMapper;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    private KcIdpUserRequestSender<KcClient> kcIdpUserRequestSender;
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
    public void createUser_WhenGivenClientAndUser_ShouldReturnUser()
            throws Exception {
        KcClient client = buildClientObject();

        User user = buildUserObject();
        Map<String, Object> userMap = buildUserMap(user);

        when(kcIdpObjectMapper.mapUserToMap(user)).thenReturn(userMap);
        when(restTemplate.exchange(
                kcIdpRequestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/users",
                HttpMethod.POST,
                kcIdpRequestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        userMap,
                        null
                ),
                Map.class
        )).thenReturn(null);

        assertEquals(user, kcIdpUserRequestSender.createUser(client, user));
    }

    @Test
    public void getUser_WhenGivenClientAndUsername_ShouldReturnUser()
            throws Exception {
        KcClient client = buildClientObject();

        User user = buildUserObject();
        Map<String, Object> userMap = buildUserMap(user);

        List<Map<String, Object>> userMaps = new ArrayList<>();
        userMaps.add(userMap);

        when(kcIdpObjectMapper.mapUserMapToUser(userMap)).thenReturn(user);
        when(restTemplate.exchange(
                kcIdpRequestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/users",
                HttpMethod.GET,
                kcIdpRequestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        null,
                        null
                ),
                List.class
        )).thenReturn(new ResponseEntity<>(userMaps, HttpStatus.OK));
        when(restTemplate.exchange(
                kcIdpRequestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/users/" + userMap.get("id"),
                HttpMethod.GET,
                kcIdpRequestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        userMap,
                        null
                ),
                Map.class
        )).thenReturn(new ResponseEntity<>(userMap, HttpStatus.OK));

        assertEquals(user, kcIdpUserRequestSender.getUser(client, user.getUsername()));
    }

    @Test
    public void getUsers_WhenGivenClient_ShouldReturnUsersList()
            throws Exception {
        KcClient client = buildClientObject();

        User user = buildUserObject();
        Map<String, Object> userMap = buildUserMap(user);

        List<Map<String, Object>> userMaps = new ArrayList<>();
        userMaps.add(userMap);

        when(kcIdpObjectMapper.mapUserMapToUser(userMap)).thenReturn(user);
        when(restTemplate.exchange(
                kcIdpRequestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/users",
                HttpMethod.GET,
                kcIdpRequestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        null,
                        null
                ),
                List.class
        )).thenReturn(new ResponseEntity<>(userMaps, HttpStatus.OK));

        assertEquals(user.getUsername(), kcIdpUserRequestSender.getUsers(client).get(0).getUsername());
    }

    private KcClient buildClientObject() {
        KcClient client = new KcClient();
        client.setId("test-id");

        return client;
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

    private Map<String, Object> buildUserMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", "test-id");
        userMap.put("username", user.getUsername());
        userMap.put("firstName", user.getFirstname());
        userMap.put("lastName", user.getLastname());
        userMap.put("email", user.getEmail());
        userMap.put("credentials", buildUserCredentialsMap(user.getPassword()));

        return userMap;
    }

    private Map<String, Object> buildUserCredentialsMap(String password) {
        Map<String, Object> userCredentials = new HashMap<>();
        userCredentials.put("value", password);
        userCredentials.put("temporary", false);

        return userCredentials;
    }
}
