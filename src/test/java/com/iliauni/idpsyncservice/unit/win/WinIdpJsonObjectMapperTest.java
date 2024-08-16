package com.iliauni.idpsyncservice.unit.win;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iliauni.idpsyncservice.exception.UserToJsonMappingException;
import com.iliauni.idpsyncservice.exception.UsergroupToJsonMappingException;
import com.iliauni.idpsyncservice.idp.IdpMapObjectMapper;
import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.model.Usergroup;
import com.iliauni.idpsyncservice.win.WinIdpJsonObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class WinIdpJsonObjectMapperTest {
    @Mock
    private IdpMapObjectMapper mapObjectMapper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private WinIdpJsonObjectMapper winIdpJsonObjectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void mapUserToJsonString_Success() throws Exception {
        // Given
        User user = new User("testUser");
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", "testUser");
        when(mapObjectMapper.mapUserToMap(user)).thenReturn(userMap);
        when(objectMapper.writeValueAsString(userMap)).thenReturn("expectedJsonString");

        // When
        String jsonString = winIdpJsonObjectMapper.mapUserToJsonString(user);

        // Then
        assertEquals("expectedJsonString", jsonString);
        verify(mapObjectMapper).mapUserToMap(user);
        verify(objectMapper).writeValueAsString(userMap);
    }

    @Test
    void mapUserToJsonString_ExceptionThrown() throws Exception {
        // Given
        User user = new User("testUser");
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", "testUser");
        when(mapObjectMapper.mapUserToMap(user)).thenReturn(userMap);
        when(objectMapper.writeValueAsString(userMap)).thenThrow(JsonProcessingException.class);

        // When
        try {
            winIdpJsonObjectMapper.mapUserToJsonString(user);
        } catch (UserToJsonMappingException e) {
            // Then
            assertEquals("An exception occurred while mapping a Win user to a JSON string: N/A", e.getMessage());
            assertEquals(JsonProcessingException.class, e.getCause().getClass());
        }

        verify(mapObjectMapper).mapUserToMap(user);
        verify(objectMapper).writeValueAsString(userMap);
    }

    @Test
    void mapUsergroupToJsonString_Success() throws Exception {
        // Given
        Usergroup usergroup = new Usergroup("group1", "Group 1", List.of(new User("user1"), new User("user2")));
        Map<String, Object> usergroupMap = new HashMap<>();
        usergroupMap.put("name", "group1");
        usergroupMap.put("description", "Group 1");
        usergroupMap.put("users", List.of("user1", "user2"));
        when(mapObjectMapper.mapUsergroupToMap(usergroup)).thenReturn(usergroupMap);
        when(objectMapper.writeValueAsString(usergroupMap)).thenReturn("expectedJsonString");

        // When
        String jsonString = winIdpJsonObjectMapper.mapUsergroupToJsonString(usergroup);

        // Then
        assertEquals("expectedJsonString", jsonString);
        verify(mapObjectMapper).mapUsergroupToMap(usergroup);
        verify(objectMapper).writeValueAsString(usergroupMap);
    }

    @Test
    void mapUsergroupToJsonString_ExceptionThrown() throws Exception {
        // Given
        Usergroup usergroup = new Usergroup("group1", "Group 1", List.of(new User("user1"), new User("user2")));
        Map<String, Object> usergroupMap = new HashMap<>();
        usergroupMap.put("name", "group1");
        usergroupMap.put("description", "Group 1");
        usergroupMap.put("users", List.of("user1", "user2"));
        when(mapObjectMapper.mapUsergroupToMap(usergroup)).thenReturn(usergroupMap);
        when(objectMapper.writeValueAsString(usergroupMap)).thenThrow(JsonProcessingException.class);

        // When
        try {
            winIdpJsonObjectMapper.mapUsergroupToJsonString(usergroup);
        } catch (UsergroupToJsonMappingException e) {
            // Then
            assertEquals("An exception occurred while mapping a Win user group to a JSON string: N/A", e.getMessage());
            assertEquals(JsonProcessingException.class, e.getCause().getClass());
        }

        verify(mapObjectMapper).mapUsergroupToMap(usergroup);
        verify(objectMapper).writeValueAsString(usergroupMap);
    }

    @Test
    void buildCredentialsRepresentation_Success() throws Exception {
        // Given
        String password = "password";
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("password", password);

        // When
        when(mapObjectMapper.buildUserCredentialsMap(password)).thenReturn(credentials);
        when(objectMapper.writeValueAsString(credentials)).thenReturn("{" + password + "}");
        String result = winIdpJsonObjectMapper.buildCredentialsRepresentation(password);

        // Then
        assertEquals("{" + password + "}", result);
    }

    @Test
    void mapUserJsonNodeToUser_Success() {
        // Given
        ObjectNode userJsonNode = JsonNodeFactory.instance.objectNode();
        userJsonNode.put("username", "testUser");

        // When
        User result = winIdpJsonObjectMapper.mapUserJsonNodeToUser(userJsonNode);

        // Then
        assertEquals("testUser", result.getUsername());
    }

    @Test
    void mapUsergroupJsonNodeToUsergroup_Success() {
        // Given
        ObjectNode usergroupJsonNode = JsonNodeFactory.instance.objectNode();
        usergroupJsonNode.put("name", "group1");
        usergroupJsonNode.put("description", "Group 1");

        ArrayNode usersNode = usergroupJsonNode.putArray("users");
        usersNode.add("user1");
        usersNode.add("user2");

        // When
        Usergroup result = winIdpJsonObjectMapper.mapUsergroupJsonNodeToUsergroup(usergroupJsonNode);

        // Then
        assertEquals("group1", result.getName());
        assertEquals("Group 1", result.getDescription());
        assertEquals(2, result.getUsers().size());
        assertEquals("user1", result.getUsers().get(0).getUsername());
        assertEquals("user2", result.getUsers().get(1).getUsername());
    }
}
