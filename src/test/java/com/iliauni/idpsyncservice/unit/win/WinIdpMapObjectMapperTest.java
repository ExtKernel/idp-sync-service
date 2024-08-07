package com.iliauni.idpsyncservice.unit.win;

import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.model.Usergroup;
import com.iliauni.idpsyncservice.win.WinIdpMapObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WinIdpMapObjectMapperTest {

    private WinIdpMapObjectMapper mapper;

    // Initialize the WinIdpMapObjectMapper instance before each test
    @BeforeEach
    void setUp() {
        mapper = new WinIdpMapObjectMapper();
    }

    // Test the mapUserToMap method to ensure it correctly maps User object to a Map
    @Test
    void testMapUserToMap() {
        // Create a User object and set its properties
        User user = new User();
        user.setUsername("testuser");
        user.setFirstname("Test");
        user.setLastname("User");
        user.setPassword("password123");

        // Call the mapUserToMap method
        Map<String, Object> userMap = mapper.mapUserToMap(user);

        // Verify that the map contains the correct values
        assertEquals("testuser", userMap.get("username"));
        assertEquals("Test", userMap.get("firstName"));
        assertEquals("User", userMap.get("lastName"));
        assertEquals("password123", userMap.get("password"));
    }

    // Test the mapUsergroupToMap method to ensure it correctly maps Usergroup object to a Map
    @Test
    void testMapUsergroupToMap() {
        // Create a Usergroup object and set its properties
        Usergroup usergroup = new Usergroup();
        usergroup.setName("testgroup");
        usergroup.setDescription("Test Group Description");

        // Call the mapUsergroupToMap method
        Map<String, Object> usergroupMap = mapper.mapUsergroupToMap(usergroup);

        // Verify that the map contains the correct values
        assertEquals("testgroup", usergroupMap.get("name"));
        assertEquals("Test Group Description", usergroupMap.get("description"));
    }

    // Test the buildUserCredentialsMap method to ensure it correctly builds a credentials map
    @Test
    void testBuildUserCredentialsMap() {
        // Define a password
        String password = "password123";

        // Call the buildUserCredentialsMap method
        Map<String, Object> credentialsMap = mapper.buildUserCredentialsMap(password);

        // Verify that the map contains the correct password value
        assertEquals("password123", credentialsMap.get("value"));
    }
}
