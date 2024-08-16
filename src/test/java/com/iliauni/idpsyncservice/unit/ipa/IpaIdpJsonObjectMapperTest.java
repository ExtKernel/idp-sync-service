package com.iliauni.idpsyncservice.unit.ipa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iliauni.idpsyncservice.exception.UserToJsonMappingException;
import com.iliauni.idpsyncservice.exception.UsergroupToJsonMappingException;
import com.iliauni.idpsyncservice.idp.IdpMapObjectMapper;
import com.iliauni.idpsyncservice.ipa.IpaIdpJsonObjectMapper;
import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.model.Usergroup;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class IpaIdpJsonObjectMapperTest {

    @Mock
    private IdpMapObjectMapper mapObjectMapper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private IpaIdpJsonObjectMapper ipaIdpJsonObjectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void mapUserToJsonString_Success() throws Exception {
        // Given
        User user = new User("testUser", "John", "Doe", "john.doe@example.com");
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("givenname", "John");
        userMap.put("sn", "Doe");
        userMap.put("mail", "john.doe@example.com");
        userMap.put("cn", "users");
        when(mapObjectMapper.mapUserToMap(user)).thenReturn(userMap);
        when(objectMapper.writeValueAsString(userMap)).thenReturn("expectedJsonString");

        // When
        String jsonString = ipaIdpJsonObjectMapper.mapUserToJsonString(user);

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
        userMap.put("givenname", "John");
        userMap.put("sn", "Doe");
        userMap.put("mail", "john.doe@example.com");
        userMap.put("cn", "users");
        when(mapObjectMapper.mapUserToMap(user)).thenReturn(userMap);
        when(objectMapper.writeValueAsString(userMap)).thenThrow(JsonProcessingException.class);

        // When
        try {
            ipaIdpJsonObjectMapper.mapUserToJsonString(user);
        } catch (UserToJsonMappingException e) {
            // Then
            assertEquals("An exception occurred while mapping a FreeIPA user to a JSON string: N/A", e.getMessage());
            assertEquals(JsonProcessingException.class, e.getCause().getClass());
        }

        verify(mapObjectMapper).mapUserToMap(user);
        verify(objectMapper).writeValueAsString(userMap);
    }

    @Test
    void mapUsergroupToJsonString_Success() throws Exception {
        // Given
        Usergroup usergroup = new Usergroup("group1", "Description for group1");
        Map<String, Object> usergroupMap = new HashMap<>();
        usergroupMap.put("description", "Description for group1");
        when(mapObjectMapper.mapUsergroupToMap(usergroup)).thenReturn(usergroupMap);
        when(objectMapper.writeValueAsString(usergroupMap)).thenReturn("expectedJsonString");

        // When
        String jsonString = ipaIdpJsonObjectMapper.mapUsergroupToJsonString(usergroup);

        // Then
        assertEquals("expectedJsonString", jsonString);
        verify(mapObjectMapper).mapUsergroupToMap(usergroup);
        verify(objectMapper).writeValueAsString(usergroupMap);
    }

    @Test
    void mapUsergroupToJsonString_ExceptionThrown() throws Exception {
        // Given
        Usergroup usergroup = new Usergroup("group1", "Description for group1");
        Map<String, Object> usergroupMap = new HashMap<>();
        usergroupMap.put("description", "Description for group1");
        when(mapObjectMapper.mapUsergroupToMap(usergroup)).thenReturn(usergroupMap);
        when(objectMapper.writeValueAsString(usergroupMap)).thenThrow(JsonProcessingException.class);

        // When
        try {
            ipaIdpJsonObjectMapper.mapUsergroupToJsonString(usergroup);
        } catch (UsergroupToJsonMappingException e) {
            // Then
            assertEquals("An exception occurred while mapping a FreeIPA user group to a JSON string: N/A", e.getMessage());
            assertEquals(JsonProcessingException.class, e.getCause().getClass());
        }

        verify(mapObjectMapper).mapUsergroupToMap(usergroup);
        verify(objectMapper).writeValueAsString(usergroupMap);
    }

    @Test
    void buildCredentialsRepresentation_Success() {
        // Given
        String password = "password";

        // When
        String result = ipaIdpJsonObjectMapper.buildCredentialsRepresentation(password);

        // Then
        assertEquals(password, result);
    }

    @Test
    void mapUserJsonNodeToUser_Success() {
        // Given
        ObjectNode userJsonNode = JsonNodeFactory.instance.objectNode();
        userJsonNode.set("uid", JsonNodeFactory.instance.arrayNode().add("testUser"));
        userJsonNode.set("givenname", JsonNodeFactory.instance.arrayNode().add("John"));
        userJsonNode.set("sn", JsonNodeFactory.instance.arrayNode().add("Doe"));
        userJsonNode.set("mail", JsonNodeFactory.instance.arrayNode().add("john.doe@example.com"));

        // When
        User result = ipaIdpJsonObjectMapper.mapUserJsonNodeToUser(userJsonNode);

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
        usergroupJsonNode.put("cn", JsonNodeFactory.instance.arrayNode().add("group1"));
        usergroupJsonNode.put("description", JsonNodeFactory.instance.arrayNode().add("Description for group1"));

        // When
        Usergroup result = ipaIdpJsonObjectMapper.mapUsergroupJsonNodeToUsergroup(usergroupJsonNode);

        // Then
        assertEquals("group1", result.getName());
        assertEquals("Description for group1", result.getDescription());
    }
}
