package com.iliauni.usersyncglobalservice.unit.idp.ipa;

import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.idp.ipa.IpaIdpObjectMapper;
import com.iliauni.usersyncglobalservice.idp.ipa.IpaIdpRequestBuilder;
import com.iliauni.usersyncglobalservice.idp.ipa.IpaIdpUserRequestSender;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.model.User;
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

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IpaIdpUserRequestSenderTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private IpaIdpRequestBuilder ipaIdpRequestBuilder;

    @Mock
    private IpaIdpObjectMapper ipaIdpObjectMapper;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    private IpaIdpUserRequestSender ipaIdpUsergroupRequestSender;
    private final String ipaHostname = "test-hostname";
    private final String ipaApiEndpoint = "test-api-endpoint";

    @BeforeEach
    public void setUpRestTemplateBuilder() {
        when(restTemplateBuilder.errorHandler(Mockito.any())).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.errorHandler(any(RestTemplateResponseErrorHandler.class)).build()).thenReturn(restTemplate);

        ipaIdpUsergroupRequestSender = new IpaIdpUserRequestSender(
                ipaIdpRequestBuilder,
                ipaIdpObjectMapper,
                restTemplateBuilder
        );
    }

    @Test
    public void createUser_WhenGivenClientAndUser_ShouldReturnUser()
            throws Exception {
        User user = buildUserObject();
        Map<String, Object> userMap = buildUserMap(user);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "user_add");
        requestBody.put("params", new Object[]{"", userMap});

        IpaClient client = buildClientObject();
        when(ipaIdpObjectMapper.mapUserToMap(user)).thenReturn(userMap);
        when(restTemplate.exchange(
                ipaIdpRequestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                ipaIdpRequestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        requestBody,
                        null
                ),
                Map.class
        )).thenReturn(null);

        assertEquals(user, ipaIdpUsergroupRequestSender.createUser(client, user));
    }

    @Test
    public void getUser_WhenGivenClientAndUsername_ShouldReturnUser()
            throws Exception {
        User user = buildUserObject();
        Map<String, Object> userMap = buildUserMap(user);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "user_show");
        requestBody.put("params", new Object[]{user.getUsername(), new HashMap<>()});

        String ipaJsonOutput = "{\n" +
                "  \"result\": {\n" +
                "    \"result\": {\n" +
                "      \"gidnumber\": [\"231600004\"],\n" +
                "      \"uidnumber\": [\"231600004\"],\n" +
                "      \"loginshell\": [\"/bin/sh\"],\n" +
                "      \"krbcanonicalname\": [\"test123@TEST.ISUCC.ILIAUNI.EDU.GE\"],\n" +
                "      \"mail\": [\"" + user.getEmail() + "\"],\n" +
                "      \"sn\": [\"" + user.getLastname() + "\"],\n" +
                "      \"givenname\": [\"" + user.getFirstname() + "\"],\n" +
                "      \"krbprincipalname\": [\"test123@TEST.ISUCC.ILIAUNI.EDU.GE\"],\n" +
                "      \"uid\": [\"" + user.getUsername() +"\"],\n" +
                "      \"homedirectory\": [\"/home/test123\"],\n" +
                "      \"nsaccountlock\": false,\n" +
                "      \"has_password\": true,\n" +
                "      \"has_keytab\": true,\n" +
                "      \"memberof_group\": [\"ipausers\"],\n" +
                "      \"dn\": \"uid=test123,cn=users,cn=accounts,dc=test,dc=isucc,dc=iliauni,dc=edu,dc=ge\"\n" +
                "    },\n" +
                "    \"value\": \"test123\",\n" +
                "    \"messages\": [\n" +
                "      {\n" +
                "        \"type\": \"warning\",\n" +
                "        \"name\": \"VersionMissing\",\n" +
                "        \"message\": \"API Version number was not sent, forward compatibility not guaranteed. Assuming server's API version, 2.252\",\n" +
                "        \"code\": 13001,\n" +
                "        \"data\": {\n" +
                "          \"server_version\": \"2.252\"\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"summary\": null\n" +
                "  },\n" +
                "  \"error\": null,\n" +
                "  \"id\": 0,\n" +
                "  \"principal\": \"admin@TEST.ISUCC.ILIAUNI.EDU.GE\",\n" +
                "  \"version\": \"4.10.2\"\n" +
                "}";

        IpaClient client = buildClientObject();
        when(ipaIdpObjectMapper.mapUserMapToUser(userMap)).thenReturn(user);
        when(restTemplate.exchange(
                ipaIdpRequestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                ipaIdpRequestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        requestBody,
                        null
                ),
                String.class
        )).thenReturn(new ResponseEntity<>(ipaJsonOutput, HttpStatus.OK));

        assertEquals(user, ipaIdpUsergroupRequestSender.getUser(client, user.getUsername()));
    }

    @Test
    public void getUsers_WhenGivenClient_ShouldReturnUsersList()
            throws Exception {
        User user = buildUserObject();
        Map<String, Object> userMap = buildUserMap(user);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", "user_find");
        requestBody.put("params", new Object[]{"", new HashMap<>()});

        String ipaJsonOutput = "{\n" +
                "  \"result\": {\n" +
                "    \"result\": [\n" +
                "      {\n" +
                "        \"krbcanonicalname\": [\"test123@TEST.ISUCC.ILIAUNI.EDU.GE\"],\n" +
                "        \"loginshell\": [\"/bin/sh\"],\n" +
                "        \"mail\": [\"" + user.getEmail() + "\"],\n" +
                "        \"homedirectory\": [\"/home/test123\"],\n" +
                "        \"uid\": [\"" + user.getUsername() + "\"],\n" +
                "        \"gidnumber\": [\"231600004\"],\n" +
                "        \"givenname\": [\"" + user.getFirstname() + "\"],\n" +
                "        \"sn\": [\"" + user.getLastname() + "\"],\n" +
                "        \"uidnumber\": [\"231600004\"],\n" +
                "        \"krbprincipalname\": [\"test123@TEST.ISUCC.ILIAUNI.EDU.GE\"],\n" +
                "        \"nsaccountlock\": false,\n" +
                "        \"dn\": \"uid=test123,cn=users,cn=accounts,dc=test,dc=isucc,dc=iliauni,dc=edu,dc=ge\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"krbcanonicalname\": [\"test213123@TEST.ISUCC.ILIAUNI.EDU.GE\"],\n" +
                "        \"loginshell\": [\"/bin/sh\"],\n" +
                "        \"mail\": [\"test213123@test.isucc.iliauni.edu.ge\"],\n" +
                "        \"homedirectory\": [\"/home/test213123\"],\n" +
                "        \"uid\": [\"test213123\"],\n" +
                "        \"gidnumber\": [\"231600005\"],\n" +
                "        \"givenname\": [\"r\"],\n" +
                "        \"sn\": [\"r\"],\n" +
                "        \"uidnumber\": [\"231600005\"],\n" +
                "        \"krbprincipalname\": [\"test213123@TEST.ISUCC.ILIAUNI.EDU.GE\"],\n" +
                "        \"nsaccountlock\": false,\n" +
                "        \"dn\": \"uid=test213123,cn=users,cn=accounts,dc=test,dc=isucc,dc=iliauni,dc=edu,dc=ge\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"count\": 3,\n" +
                "    \"truncated\": false,\n" +
                "    \"messages\": [\n" +
                "      {\n" +
                "        \"type\": \"warning\",\n" +
                "        \"name\": \"VersionMissing\",\n" +
                "        \"message\": \"API Version number was not sent, forward compatibility not guaranteed. Assuming server's API version, 2.252\",\n" +
                "        \"code\": 13001,\n" +
                "        \"data\": {\n" +
                "          \"server_version\": \"2.252\"\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"summary\": \"3 users matched\"\n" +
                "  },\n" +
                "  \"error\": null,\n" +
                "  \"id\": 0,\n" +
                "  \"principal\": \"admin@TEST.ISUCC.ILIAUNI.EDU.GE\",\n" +
                "  \"version\": \"4.10.2\"\n" +
                "}";

        IpaClient client = buildClientObject();
        when(ipaIdpObjectMapper.mapUserMapToUser(userMap)).thenReturn(user);
        when(restTemplate.exchange(
                ipaIdpRequestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                ipaIdpRequestBuilder.buildHttpRequestEntity(
                        client.getId(),
                        requestBody,
                        null
                ),
                String.class
        )).thenReturn(new ResponseEntity<>(ipaJsonOutput, HttpStatus.OK));

        assertEquals(user.getUsername(), ipaIdpUsergroupRequestSender.getUsers(client).get(0).getUsername());
    }

    private IpaClient buildClientObject() {
        IpaClient client = new IpaClient();
        client.setId("test-id");

        return client;
    }

    private User buildUserObject() {
        User user = new User();
        user.setUsername("test-username");
        user.setFirstname("test-firstname");
        user.setLastname("test-lastname");
        user.setEmail("test-email@test.com");

        return user;
    }

    private Map<String, Object> buildUserMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("uid", user.getUsername());
        userMap.put("givenname", user.getFirstname());
        userMap.put("sn", user.getLastname());
        userMap.put("mail", user.getEmail());

        return userMap;
    }
}
