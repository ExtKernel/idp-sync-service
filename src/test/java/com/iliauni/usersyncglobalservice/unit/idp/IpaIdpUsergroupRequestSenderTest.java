package com.iliauni.usersyncglobalservice.unit.idp;

import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.idp.IpaIdpObjectMapper;
import com.iliauni.usersyncglobalservice.idp.IpaIdpRequestBuilder;
import com.iliauni.usersyncglobalservice.idp.IpaIdpUsergroupRequestSender;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IpaIdpUsergroupRequestSenderTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private IpaIdpRequestBuilder<IpaClient> ipaIdpRequestBuilder;

    @Mock
    private IpaIdpObjectMapper ipaIdpObjectMapper;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    private IpaIdpUsergroupRequestSender<IpaClient> ipaIdpUsergroupRequestSender;
    private final String ipaHostname = "test-hostname";
    private final String ipaApiEndpoint = "test-api-endpoint";

    @BeforeEach
    public void setUpRestTemplateBuilder() {
        when(restTemplateBuilder.errorHandler(Mockito.any())).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.errorHandler(any(RestTemplateResponseErrorHandler.class)).build()).thenReturn(restTemplate);

        ipaIdpUsergroupRequestSender = new IpaIdpUsergroupRequestSender<>(
                ipaIdpRequestBuilder,
                ipaIdpObjectMapper,
                restTemplateBuilder
        );
    }

    @Test
    public void createUsergroup_WhenGivenClientAndUsergroup_ShouldReturnUsergroup()
            throws Exception {
        IpaClient client = new IpaClient();
        client.setId("test-id");

        Usergroup usergroup = buildUsergroupObject();
        MultiValueMap<String, Object> usergroupMap = buildUsergroupMap(usergroup);

        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("method", "group_add");
        requestBody.add("params", new Object[]{"", usergroupMap});

        when(ipaIdpObjectMapper.mapUsergroupToMap(usergroup)).thenReturn(usergroupMap);
        when(restTemplate.exchange(
                ipaIdpRequestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                ipaIdpRequestBuilder.buildHttpRequestEntity(client.getId(), requestBody),
                Map.class
        )).thenReturn(null);

        assertEquals(usergroup, ipaIdpUsergroupRequestSender.createUsergroup(client, usergroup));
    }

    @Test
    public void getUsergroup_WhenGivenClientAndUsergroupName_ShouldReturnUsergroup()
            throws Exception {
        IpaClient client = new IpaClient();
        client.setId("test-id");

        Usergroup usergroup = buildUsergroupObject();
        MultiValueMap<String, Object> usergroupMap = buildUsergroupMap(usergroup);

        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("method", "group_show");
        requestBody.add("params", new Object[]{usergroup.getName(), new HashMap<>()});

        String ipaJsonOutput = "{" +
                "\"result\": {" +
                "\"result\": {" +
                "\"cn\": [\"" + usergroup.getName() + "\"]," +
                "\"gidnumber\": [\"231600000\"]," +
                "\"description\": [\"" + usergroup.getDescription() + "\"]," +
                "\"member_user\": [\"admin\"]," +
                "\"dn\": \"cn=admins,cn=groups,cn=accounts,dc=test,dc=isucc,dc=iliauni,dc=edu,dc=ge\"" +
                "}," +
                "\"value\": \"admins\"," +
                "\"messages\": [{" +
                "\"type\": \"warning\"," +
                "\"name\": \"VersionMissing\"," +
                "\"message\": \"API Version number was not sent, forward compatibility not guaranteed. Assuming server's API version, 2.252\"," +
                "\"code\": 13001," +
                "\"data\": {" +
                "\"server_version\": \"2.252\"" +
                "}" +
                "}]," +
                "\"summary\": null" +
                "}," +
                "\"error\": null," +
                "\"id\": 0," +
                "\"principal\": \"admin@TEST.ISUCC.ILIAUNI.EDU.GE\"," +
                "\"version\": \"4.10.2\"" +
                "}";

        when(ipaIdpObjectMapper.mapUsergroupMapToUsergroup(usergroupMap)).thenReturn(usergroup);
        when(restTemplate.exchange(
                ipaIdpRequestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                ipaIdpRequestBuilder.buildHttpRequestEntity(client.getId(), requestBody),
                String.class
        )).thenReturn(new ResponseEntity<>(ipaJsonOutput, HttpStatus.OK));

        assertEquals(usergroup, ipaIdpUsergroupRequestSender.getUsergroup(client, usergroup.getName()));
    }

    @Test
    public void getUsergroups_WhenGivenClient_ShouldReturnUsergroupList()
            throws Exception {
        IpaClient client = new IpaClient();
        client.setId("test-id");

        Usergroup usergroup = buildUsergroupObject();
        MultiValueMap<String, Object> usergroupMap = buildUsergroupMap(usergroup);

        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("method", "group_show");
        requestBody.add("params", new Object[]{usergroup.getName(), new HashMap<>()});

        String ipaJsonOutput = "{\n" +
                "  \"result\": {\n" +
                "    \"result\": [\n" +
                "      {\n" +
                "        \"description\": [\"" + usergroup.getDescription() + "\"],\n" +
                "        \"gidnumber\": [\"231600000\"],\n" +
                "        \"cn\": [\"" + usergroup.getName() + "\"],\n" +
                "        \"dn\": \"cn=admins,cn=groups,cn=accounts,dc=test,dc=isucc,dc=iliauni,dc=edu,dc=ge\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"description\": [\"Limited admins who can edit other users\"],\n" +
                "        \"gidnumber\": [\"231600002\"],\n" +
                "        \"cn\": [\"editors\"],\n" +
                "        \"dn\": \"cn=editors,cn=groups,cn=accounts,dc=test,dc=isucc,dc=iliauni,dc=edu,dc=ge\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"description\": [\"Default group for all users\"],\n" +
                "        \"cn\": [\"ipausers\"],\n" +
                "        \"dn\": \"cn=ipausers,cn=groups,cn=accounts,dc=test,dc=isucc,dc=iliauni,dc=edu,dc=ge\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"gidnumber\": [\"231600006\"],\n" +
                "        \"cn\": [\"testsdf\"],\n" +
                "        \"dn\": \"cn=testsdf,cn=groups,cn=accounts,dc=test,dc=isucc,dc=iliauni,dc=edu,dc=ge\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"description\": [\"Trusts administrators group\"],\n" +
                "        \"cn\": [\"trust admins\"],\n" +
                "        \"dn\": \"cn=trust admins,cn=groups,cn=accounts,dc=test,dc=isucc,dc=iliauni,dc=edu,dc=ge\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"count\": 5,\n" +
                "    \"truncated\": false,\n" +
                "    \"messages\": [\n" +
                "      {\n" +
                "        \"type\": \"warning\",\n" +
                "        \"name\": \"VersionMissing\",\n" +
                "        \"message\": \"API Version number was not sent, forward compatibility not guaranteed. Assumin* TLSv1.2 (IN), TLS header, Unknown (23):\\n* Connection #0 to host ipa01.test.isucc.iliauni.edu.ge left intact\\ng server's API version, 2.252\",\n" +
                "        \"code\": 13001,\n" +
                "        \"data\": {\"server_version\": \"2.252\"}\n" +
                "      }\n" +
                "    ],\n" +
                "    \"summary\": \"5 groups matched\"\n" +
                "  },\n" +
                "  \"error\": null,\n" +
                "  \"id\": 0,\n" +
                "  \"principal\": \"admin@TEST.ISUCC.ILIAUNI.EDU.GE\",\n" +
                "  \"version\": \"4.10.2\"\n" +
                "}";

        when(ipaIdpObjectMapper.mapUsergroupMapToUsergroup(usergroupMap)).thenReturn(usergroup);
        when(restTemplate.exchange(
                ipaIdpRequestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                ipaIdpRequestBuilder.buildHttpRequestEntity(client.getId(), requestBody),
                String.class
        )).thenReturn(new ResponseEntity<>(ipaJsonOutput, HttpStatus.OK));

        assertEquals(usergroup.getName(), ipaIdpUsergroupRequestSender.getUsergroups(client).get(0).getName());
    }

    private Usergroup buildUsergroupObject() {
        Usergroup usergroup = new Usergroup();
        usergroup.setName("test-name");
        usergroup.setDescription("test-description");

        return usergroup;
    }

    private MultiValueMap<String, Object> buildUsergroupMap(Usergroup usergroup) {
        MultiValueMap<String, Object> usergroupMap = new LinkedMultiValueMap<>();
        usergroupMap.add("cn", usergroup.getName());
        usergroupMap.add("description", usergroup.getDescription());

        return usergroupMap;
    }
}
