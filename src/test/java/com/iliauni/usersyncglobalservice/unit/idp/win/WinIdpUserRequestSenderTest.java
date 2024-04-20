package com.iliauni.usersyncglobalservice.unit.idp.win;

import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.idp.win.WinIdpObjectMapper;
import com.iliauni.usersyncglobalservice.idp.win.WinIdpRequestBuilder;
import com.iliauni.usersyncglobalservice.idp.win.WinIdpUserRequestSender;
import com.iliauni.usersyncglobalservice.idp.win.WinIdpUsergroupRequestSender;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import com.iliauni.usersyncglobalservice.model.WinClient;
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
public class WinIdpUserRequestSenderTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private WinIdpRequestBuilder winIdpRequestBuilder;

    @Mock
    private WinIdpObjectMapper winIdpObjectMapper;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    private WinIdpUserRequestSender winIdpUserRequestSender;
    private final String hostname = "test-hostname";
    private String endpoint = "/test-endpoint/";

    @BeforeEach
    public void setUpRestTemplateBuilder() {
        when(restTemplateBuilder.errorHandler(Mockito.any())).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.errorHandler(any(RestTemplateResponseErrorHandler.class)).build()).thenReturn(restTemplate);

        winIdpUserRequestSender = new WinIdpUserRequestSender(
                winIdpRequestBuilder,
                winIdpObjectMapper,
                restTemplateBuilder
        );
    }

    @Test
    public void createUser_WhenGivenClientAndUser_ShouldReturnUser()
            throws Exception {
        User user = buildUserObject();
        Map<String, Object> userMap = buildUserMap(user);

        WinClient client = buildClientObject();
        when(winIdpObjectMapper.mapUserToMap(user)).thenReturn(userMap);
        when(restTemplate.exchange(
                winIdpRequestBuilder.buildApiBaseUrl(hostname, "/" + user.getUsername() + "/"),
                HttpMethod.POST,
                winIdpRequestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        userMap,
                        null
                ),
                Map.class
        )).thenReturn(null);

        assertEquals(user, winIdpUserRequestSender.createUser(client, user));
    }

    @Test
    public void getUser_WhenGivenClientAndUsername_ShouldReturnUser()
            throws Exception {
        User user = buildUserObject();
        Map<String, Object> userMap = buildUserMap(user);

        WinClient client = buildClientObject();
        when(winIdpObjectMapper.mapUserMapToUser(userMap)).thenReturn(user);
        when(restTemplate.exchange(
                winIdpRequestBuilder.buildApiBaseUrl(hostname, "/" + user.getUsername() + "/"),
                HttpMethod.GET,
                winIdpRequestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        null,
                        null
                ),
                Map.class
        )).thenReturn(new ResponseEntity<>(userMap, HttpStatus.OK));

        assertEquals(user, winIdpUserRequestSender.getUser(client, user.getUsername()));
    }

    @Test
    public void getUser_WhenGivenClient_ShouldReturnUserList()
            throws Exception {
        User user = buildUserObject();
        Map<String, Object> userMap = buildUserMap(user);

        List<Map<String, Object>> usergroupMaps = new ArrayList<>();
        usergroupMaps.add(userMap);

        endpoint = "/";
        WinClient client = buildClientObject();
        when(winIdpObjectMapper.mapUserMapToUser(userMap)).thenReturn(user);
        when(restTemplate.exchange(
                winIdpRequestBuilder.buildApiBaseUrl(hostname, endpoint),
                HttpMethod.GET,
                winIdpRequestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        null,
                        null
                ),
                List.class
        )).thenReturn(new ResponseEntity<>(usergroupMaps, HttpStatus.OK));

        assertEquals(user.getUsername(), winIdpUserRequestSender.getUsers(client).get(0).getUsername());
    }

    private WinClient buildClientObject() {
        WinClient client = new WinClient();
        client.setId("test-id");
        client.setFqdn("test.fqdn");

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
        userMap.put("username", user.getUsername());
        userMap.put("firstName", user.getFirstname());
        userMap.put("lastName", user.getLastname());
        userMap.put("email", user.getEmail());

        return userMap;
    }
}
