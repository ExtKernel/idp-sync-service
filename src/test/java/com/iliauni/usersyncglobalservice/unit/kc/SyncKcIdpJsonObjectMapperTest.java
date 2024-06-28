package com.iliauni.usersyncglobalservice.unit.kc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iliauni.usersyncglobalservice.exception.CredentialsRepresentationBuildingException;
import com.iliauni.usersyncglobalservice.exception.UserToJsonMappingException;
import com.iliauni.usersyncglobalservice.exception.UsergroupToJsonMappingException;
import com.iliauni.usersyncglobalservice.idp.IdpMapObjectMapper;
import com.iliauni.usersyncglobalservice.kc.SyncKcIdpJsonObjectMapper;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SyncKcIdpJsonObjectMapperTest {

    @Mock
    private IdpMapObjectMapper mapObjectMapper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private SyncKcIdpJsonObjectMapper syncKcIdpJsonObjectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void mapUserToJsonString_Success() throws Exception {
        // Given
        User user = new User("testUser", "John", "Doe", "john.doe@example.com");
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", "testUser");
        userMap.put("firstName", "John");
        userMap.put("lastName", "Doe");
        userMap.put("email", "john.doe@example.com");
        when(mapObjectMapper.mapUserToMap(user)).thenReturn(userMap);
        when(objectMapper.writeValueAsString(userMap)).thenReturn("expectedJsonString");

        // When
        String jsonString = syncKcIdpJsonObjectMapper.mapUserToJsonString(user);

        // Then
        assertEquals("expectedJsonString", jsonString);
        verify(mapObjectMapper).mapUserToMap(user);
        verify(objectMapper).writeValueAsString(userMap);
    }

    @Test
    void mapUserToJsonString_ExceptionThrown() throws Exception {
        // Given
        User user = new User("testUser", "John", "Doe", "john.doe@example.com");
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", "testUser");
        userMap.put("firstName", "John");
        userMap.put("lastName", "Doe");
        userMap.put("email", "john.doe@example.com");
        when(mapObjectMapper.mapUserToMap(user)).thenReturn(userMap);
        when(objectMapper.writeValueAsString(userMap)).thenThrow(JsonProcessingException.class);

        // When
        try {
            syncKcIdpJsonObjectMapper.mapUserToJsonString(user);
        } catch (UserToJsonMappingException e) {
            // Then
            assertEquals("An exception occurred while mapping a Keycloak user to a JSON string: N/A", e.getMessage());
            assertEquals(JsonProcessingException.class, e.getCause().getClass());
        }

        verify(mapObjectMapper).mapUserToMap(user);
        verify(objectMapper).writeValueAsString(userMap);
    }

    @Test
    void mapUsergroupToJsonString_Success() throws Exception {
        // Given
        Usergroup usergroup = new Usergroup("group1");
        Map<String, Object> usergroupMap = new HashMap<>();
        usergroupMap.put("name", "group1");
        when(mapObjectMapper.mapUsergroupToMap(usergroup)).thenReturn(usergroupMap);
        when(objectMapper.writeValueAsString(usergroupMap)).thenReturn("expectedJsonString");

        // When
        String jsonString = syncKcIdpJsonObjectMapper.mapUsergroupToJsonString(usergroup);

        // Then
        assertEquals("expectedJsonString", jsonString);
        verify(mapObjectMapper).mapUsergroupToMap(usergroup);
        verify(objectMapper).writeValueAsString(usergroupMap);
    }

    @Test
    void mapUsergroupToJsonString_ExceptionThrown() throws Exception {
        // Given
        Usergroup usergroup = new Usergroup("group1");
        Map<String, Object> usergroupMap = new HashMap<>();
        usergroupMap.put("name", "group1");
        when(mapObjectMapper.mapUsergroupToMap(usergroup)).thenReturn(usergroupMap);
        when(objectMapper.writeValueAsString(usergroupMap)).thenThrow(JsonProcessingException.class);

        // When
        try {
            syncKcIdpJsonObjectMapper.mapUsergroupToJsonString(usergroup);
        } catch (UsergroupToJsonMappingException e) {
            // Then
            assertEquals("An exception occurred while mapping a Keycloak user group to a JSON string: N/A", e.getMessage());
            assertEquals(JsonProcessingException.class, e.getCause().getClass());
        }

        verify(mapObjectMapper).mapUsergroupToMap(usergroup);
        verify(objectMapper).writeValueAsString(usergroupMap);
    }

    @Test
    void buildCredentialsRepresentation_Success() throws Exception {
        // Given
        String password = "password";
        Map<String, Object> credentialsMap = new HashMap<>();
        credentialsMap.put("password", password);
        when(mapObjectMapper.buildUserCredentialsMap(password)).thenReturn(credentialsMap);
        when(objectMapper.writeValueAsString(credentialsMap)).thenReturn("expectedJsonString");

        // When
        String jsonString = syncKcIdpJsonObjectMapper.buildCredentialsRepresentation(password);

        // Then
        assertEquals("expectedJsonString", jsonString);
        verify(mapObjectMapper).buildUserCredentialsMap(password);
        verify(objectMapper).writeValueAsString(credentialsMap);
    }

    @Test
    void buildCredentialsRepresentation_ExceptionThrown() throws Exception {
        // Given
        String password = "password";
        Map<String, Object> credentialsMap = new HashMap<>();
        credentialsMap.put("password", password);
        when(mapObjectMapper.buildUserCredentialsMap(password)).thenReturn(credentialsMap);
        when(objectMapper.writeValueAsString(credentialsMap)).thenThrow(JsonProcessingException.class);

        // When
        try {
            syncKcIdpJsonObjectMapper.buildCredentialsRepresentation(password);
        } catch (CredentialsRepresentationBuildingException e) {
            // Then
            assertEquals("An exception occurred while building credentials representation: N/A", e.getMessage());
            assertEquals(JsonProcessingException.class, e.getCause().getClass());
        }

        verify(mapObjectMapper).buildUserCredentialsMap(password);
        verify(objectMapper).writeValueAsString(credentialsMap);
    }

    @Test
    void mapUserJsonNodeToUser_Success() {
        // Given
        ObjectNode userJsonNode = JsonNodeFactory.instance.objectNode();
        userJsonNode.put("username", "testUser");
        userJsonNode.put("firstName", "John");
        userJsonNode.put("lastName", "Doe");
        userJsonNode.put("email", "john.doe@example.com");

        // When
        User result = syncKcIdpJsonObjectMapper.mapUserJsonNodeToUser(userJsonNode);

        // Then
        assertEquals("testUser", result.getUsername());
        assertEquals("John", result.getFirstname());
        assertEquals("Doe", result.getLastname());
        assertEquals("john.doe@example.com", result.getEmail());
    }

    @Test
    void mapUsergroupJsonNodeToUsergroup_Success() {
        // Given
        ObjectNode usergroupJsonNode = JsonNodeFactory.instance.objectNode();
        usergroupJsonNode.put("name", "group1");

        // When
        Usergroup result = syncKcIdpJsonObjectMapper.mapUsergroupJsonNodeToUsergroup(usergroupJsonNode);

        // Then
        assertEquals("group1", result.getName());
    }
}
