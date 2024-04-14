package com.iliauni.usersyncglobalservice.unit.idp;

import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.idp.KcIdpObjectMapper;
import com.iliauni.usersyncglobalservice.idp.KcIdpRequestBuilder;
import com.iliauni.usersyncglobalservice.idp.KcIdpUsergroupRequestSender;
import com.iliauni.usersyncglobalservice.model.KcClient;
import com.iliauni.usersyncglobalservice.model.Usergroup;
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
public class KcIdpUsergroupRequestSenderTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private KcIdpRequestBuilder<KcClient> kcIdpRequestBuilder;

    @Mock
    private KcIdpObjectMapper kcIdpObjectMapper;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    private KcIdpUsergroupRequestSender<KcClient> kcIdpUsergroupRequestSender;
    private final String kcBaseUrl = "test-base-url";
    private final String kcRealm = "test-realm";

    @BeforeEach
    public void setUpRestTemplateBuilder() {
        when(restTemplateBuilder.errorHandler(Mockito.any())).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.errorHandler(any(RestTemplateResponseErrorHandler.class)).build()).thenReturn(restTemplate);

        kcIdpUsergroupRequestSender = new KcIdpUsergroupRequestSender<>(
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

        Usergroup usergroup = buildUsergroupObject();
        MultiValueMap<String, Object> usergroupMap = buildUsergroupMap(usergroup);

        when(kcIdpObjectMapper.mapUsergroupToMap(usergroup)).thenReturn(usergroupMap);
        when(restTemplate.exchange(
                kcIdpRequestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/groups",
                HttpMethod.POST,
                kcIdpRequestBuilder.buildHttpRequestEntity(client.getId(), usergroupMap),
                Map.class
        )).thenReturn(null);

        assertEquals(usergroup, kcIdpUsergroupRequestSender.createUsergroup(client, usergroup));
    }

    @Test
    public void getUsergroup_WhenGivenClientAndUsergroupName_ShouldReturnUsergroup()
            throws Exception {
        KcClient client = new KcClient();
        client.setId("test-id");

        Usergroup usergroup = buildUsergroupObject();
        MultiValueMap<String, Object> usergroupMap = buildUsergroupMap(usergroup);

        List<MultiValueMap<String, Object>> usergroupMaps = new ArrayList<>();
        usergroupMaps.add(usergroupMap);

        when(kcIdpObjectMapper.mapUsergroupMapToUsergroup(usergroupMap)).thenReturn(usergroup);
        when(restTemplate.exchange(
                kcIdpRequestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/groups",
                HttpMethod.GET,
                kcIdpRequestBuilder.buildHttpRequestEntity(client.getId(), null),
                List.class
        )).thenReturn(new ResponseEntity<>(usergroupMaps, HttpStatus.OK));
        when(restTemplate.exchange(
                kcIdpRequestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/groups/" + usergroupMap.get("id").get(0),
                HttpMethod.GET,
                kcIdpRequestBuilder.buildHttpRequestEntity(client.getId(), null),
                Map.class
        )).thenReturn(new ResponseEntity<>(usergroupMap, HttpStatus.OK));

        assertEquals(usergroup, kcIdpUsergroupRequestSender.getUsergroup(client, usergroup.getName()));
    }

    @Test
    public void getUsergroups_WhenGivenClient_ShouldReturnUsergroupList()
            throws Exception {
        KcClient client = new KcClient();
        client.setId("test-id");

        Usergroup usergroup = buildUsergroupObject();
        MultiValueMap<String, Object> usergroupMap = buildUsergroupMap(usergroup);

        List<MultiValueMap<String, Object>> usergroupMaps = new ArrayList<>();
        usergroupMaps.add(usergroupMap);

        when(kcIdpObjectMapper.mapUsergroupMapToUsergroup(usergroupMap)).thenReturn(usergroup);
        when(restTemplate.exchange(
                kcIdpRequestBuilder.buildApiBaseUrl(kcBaseUrl, kcRealm) + "/groups",
                HttpMethod.GET,
                kcIdpRequestBuilder.buildHttpRequestEntity(client.getId(), null),
                List.class
        )).thenReturn(new ResponseEntity<>(usergroupMaps, HttpStatus.OK));

        assertEquals(usergroup.getName(), kcIdpUsergroupRequestSender.getUsergroups(client).get(0).getName());
    }

    private Usergroup buildUsergroupObject() {
        Usergroup usergroup = new Usergroup();
        usergroup.setName("test-name");

        return usergroup;
    }

    private MultiValueMap<String, Object> buildUsergroupMap(Usergroup usergroup) {
        MultiValueMap<String, Object> usergroupMap = new LinkedMultiValueMap<>();
        usergroupMap.add("id", "test-id");
        usergroupMap.add("name", usergroup.getName());

        return usergroupMap;
    }
}
