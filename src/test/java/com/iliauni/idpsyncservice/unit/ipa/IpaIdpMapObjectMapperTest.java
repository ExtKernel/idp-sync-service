package com.iliauni.idpsyncservice.unit.ipa;

import com.iliauni.idpsyncservice.ipa.IpaIdpMapObjectMapper;
import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.model.Usergroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IpaIdpMapObjectMapperTest {

    private IpaIdpMapObjectMapper mapper;

    // Initialize the IpaIdpMapObjectMapper instance before each test
    @BeforeEach
    void setUp() {
        mapper = new IpaIdpMapObjectMapper();
    }

    // Test the mapUserToMap method to ensure it correctly maps User object to a Map
    @Test
    void testMapUserToMap() {
        // Create a User object and set its properties
        User user = new User();
        user.setFirstname("Test");
        user.setLastname("User");
        user.setEmail("testuser@example.com");

        // Call the mapUserToMap method
        Map<String, Object> userMap = mapper.mapUserToMap(user);

        // Verify that the map contains the correct values
        assertEquals("Test", userMap.get("givenname"));
        assertEquals("User", userMap.get("sn"));
        assertEquals("testuser@example.com", userMap.get("mail"));
        assertEquals("users", userMap.get("cn"));
    }

    // Test the mapUsergroupToMap method to ensure it correctly maps Usergroup object to a Map
    @Test
    void testMapUsergroupToMap() {
        // Create a Usergroup object and set its description
        Usergroup usergroup = new Usergroup();
        usergroup.setDescription("Test Group Description");

        // Call the mapUsergroupToMap method
        Map<String, Object> usergroupMap = mapper.mapUsergroupToMap(usergroup);

        // Verify that the map contains the correct description
        assertEquals("Test Group Description", usergroupMap.get("description"));
    }

    // Test the buildUserCredentialsMap method to ensure it correctly builds a credentials map
    @Test
    void testBuildUserCredentialsMap() {
        // Define a credentials map
        String password = "password123";
        Map<String, String> validCredentialsMap = new HashMap<>();
        validCredentialsMap.put("password", password);

        // Call the buildUserCredentialsMap method
        Map<String, Object> credentialsMap = mapper.buildUserCredentialsMap(password);

        // Verify that the map contains the correct password value
        assertEquals(validCredentialsMap.get("password"), credentialsMap.get("password"));
    }
}
