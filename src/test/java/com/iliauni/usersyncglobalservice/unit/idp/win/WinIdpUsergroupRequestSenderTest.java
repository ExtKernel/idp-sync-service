package com.iliauni.usersyncglobalservice.unit.idp.win;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.idp.win.WinIdpObjectMapper;
import com.iliauni.usersyncglobalservice.idp.win.WinIdpRequestBuilder;
import com.iliauni.usersyncglobalservice.idp.win.WinIdpUsergroupRequestSender;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import com.iliauni.usersyncglobalservice.model.WinClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

@ExtendWith(MockitoExtension.class)
public class WinIdpUsergroupRequestSenderTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private WinIdpRequestBuilder winIdpRequestBuilder;

    @Mock
    private WinIdpObjectMapper winIdpObjectMapper;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    private WinIdpUsergroupRequestSender winIdpUsergroupRequestSender;
    private final String hostname = "test-hostname";
    private String endpoint = "/test-endpoint/";

    @BeforeEach
    public void setUpRestTemplateBuilder() {
        when(restTemplateBuilder.errorHandler(Mockito.any())).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.errorHandler(any(RestTemplateResponseErrorHandler.class)).build()).thenReturn(restTemplate);

        winIdpUsergroupRequestSender = new WinIdpUsergroupRequestSender(
                winIdpRequestBuilder,
                winIdpObjectMapper,
                restTemplateBuilder
        );
    }

    @Test
    public void createUsergroup_WhenGivenClientAndUsergroup_ShouldReturnUsergroup()
            throws Exception {
        Usergroup usergroup = buildUsergroupObject();
        Map<String, Object> usergroupMap = buildUsergroupMap(usergroup);

        WinClient client = buildClientObject();
        when(winIdpObjectMapper.mapUsergroupToMap(usergroup)).thenReturn(usergroupMap);
        when(restTemplate.exchange(
                winIdpRequestBuilder.buildApiBaseUrl(hostname, "/" + usergroup.getName() + "/"),
                HttpMethod.POST,
                winIdpRequestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        usergroupMap,
                        null
                ),
                Map.class
        )).thenReturn(null);

        assertEquals(usergroup, winIdpUsergroupRequestSender.createUsergroup(client, usergroup));
    }

    @Test
    public void getUsergroup_WhenGivenClientAndUsergroupName_ShouldReturnUsergroup()
            throws Exception {
        Usergroup usergroup = buildUsergroupObject();
        Map<String, Object> usergroupMap = buildUsergroupMap(usergroup);

        WinClient client = buildClientObject();
        when(winIdpObjectMapper.mapUsergroupMapToUsergroup(usergroupMap)).thenReturn(usergroup);
        when(restTemplate.exchange(
                winIdpRequestBuilder.buildApiBaseUrl(hostname, "/" + usergroup.getName() + "/"),
                HttpMethod.GET,
                winIdpRequestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        null,
                        null
                ),
                Map.class
        )).thenReturn(new ResponseEntity<>(usergroupMap, HttpStatus.OK));

        assertEquals(usergroup, winIdpUsergroupRequestSender.getUsergroup(client, usergroup.getName()));
    }

    @Test
    public void getUsergroups_WhenGivenClient_ShouldReturnUsergroupList()
            throws Exception {
        Usergroup usergroup = buildUsergroupObject();
        Map<String, Object> usergroupMap = buildUsergroupMap(usergroup);

        List<Map<String, Object>> usergroupMaps = new ArrayList<>();
        usergroupMaps.add(usergroupMap);

        endpoint = "/";
        WinClient client = buildClientObject();
        when(winIdpObjectMapper.mapUsergroupMapToUsergroup(usergroupMap)).thenReturn(usergroup);
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

        assertEquals(usergroup.getName(), winIdpUsergroupRequestSender.getUsergroups(client).get(0).getName());
    }

    private WinClient buildClientObject() {
        WinClient client = new WinClient();
        client.setId("test-id");
        client.setFqdn("test.fqdn");

        return client;
    }

    private Usergroup buildUsergroupObject() {
        Usergroup usergroup = new Usergroup();
        usergroup.setName("test-name");

        return usergroup;
    }

    private Map<String, Object> buildUsergroupMap(Usergroup usergroup) {
        Map<String, Object> usergroupMap = new HashMap<>();
        usergroupMap.put("id", "test-id");
        usergroupMap.put("name", usergroup.getName());

        return usergroupMap;
    }
}
