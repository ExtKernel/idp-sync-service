package com.iliauni.usersyncglobalservice.unit.kc;

import com.iliauni.usersyncglobalservice.kc.SyncKcIdpMapObjectMapper;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SyncKcIdpMapObjectMapperTest {

    private SyncKcIdpMapObjectMapper mapper;

    // Initialize the SyncKcIdpMapObjectMapper instance before each test
    @BeforeEach
    void setUp() {
        mapper = new SyncKcIdpMapObjectMapper();
    }

    // Test the mapUserToMap method to ensure it correctly maps User object to a Map
    @Test
    void testMapUserToMap() {
        // Create a User object and set its properties
        User user = new User();
        user.setUsername("testuser");
        user.setFirstname("Test");
        user.setLastname("User");
        user.setEmail("testuser@example.com");
        user.setPassword("password123");

        // Call the mapUserToMap method
        Map<String, Object> userMap = mapper.mapUserToMap(user);

        // Verify that the map contains the correct values
        assertEquals("testuser", userMap.get("username"));
        assertEquals("Test", userMap.get("firstName"));
        assertEquals("User", userMap.get("lastName"));
        assertEquals("testuser@example.com", userMap.get("email"));

        // Verify the credentials map within the user map
        Map<String, Object> credentials = (Map<String, Object>) userMap.get("credentials");
        assertEquals("password123", credentials.get("value"));
        assertFalse((Boolean) credentials.get("temporary"));
    }

    // Test the mapUsergroupToMap method to ensure it correctly maps Usergroup object to a Map
    @Test
    void testMapUsergroupToMap() {
        // Create a Usergroup object and set its name
        Usergroup usergroup = new Usergroup();
        usergroup.setName("testgroup");

        // Call the mapUsergroupToMap method
        Map<String, Object> usergroupMap = mapper.mapUsergroupToMap(usergroup);

        // Verify that the map contains the correct name
        assertEquals("testgroup", usergroupMap.get("name"));
    }

    // Test the buildUserCredentialsMap method to ensure it correctly builds a credentials map
    @Test
    void testBuildUserCredentialsMap() {
        // Define a password
        String password = "password123";

        // Call the buildUserCredentialsMap method
        Map<String, Object> credentialsMap = mapper.buildUserCredentialsMap(password);

        // Verify that the map contains the correct password value and temporary flag
        assertEquals("password123", credentialsMap.get("value"));
        assertFalse((Boolean) credentialsMap.get("temporary"));
    }
}
