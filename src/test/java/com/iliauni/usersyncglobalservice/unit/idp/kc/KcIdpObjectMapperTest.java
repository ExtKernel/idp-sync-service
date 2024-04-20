package com.iliauni.usersyncglobalservice.unit.idp.kc;

import com.iliauni.usersyncglobalservice.idp.kc.KcIdpObjectMapper;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KcIdpObjectMapperTest {
    @Test
    public void mapUserToMap_WhenGivenUser_ShouldReturnMap()
            throws Exception {
        User user = buildUserObject();

        KcIdpObjectMapper ipaIdpObjectMapper = new KcIdpObjectMapper();
        Map<String, Object> userMap = ipaIdpObjectMapper.mapUserToMap(user);

        assertEquals(user.getUsername(), userMap.get("username"));
        assertEquals(user.getFirstname(), userMap.get("firstName"));
        assertEquals(user.getLastname(), userMap.get("lastName"));
        assertEquals(user.getEmail(), userMap.get("email"));
        assertEquals(user.getPassword(), ((Map<String, Object>) userMap.get("credentials")).get("value"));
        assertEquals(false, ((Map<String, Object>) userMap.get("credentials")).get("temporary"));
    }

    @Test
    public void mapUsergroupToMap_WhenGivenUsergroup_ShouldReturnMap()
            throws Exception {
        Usergroup usergroup = new Usergroup();
        usergroup.setName("test-name");

        KcIdpObjectMapper ipaIdpObjectMapper = new KcIdpObjectMapper();
        Map<String, Object> usergroupMap = ipaIdpObjectMapper.mapUsergroupToMap(usergroup);

        assertEquals(usergroup.getName(), usergroupMap.get("name"));
    }

    @Test
    public void mapUserMapToUser_WhenGivenUserMap_ShouldReturnUser()
            throws Exception {
        Map<String, Object> userMap = buildUserMap(buildUserObject());

        KcIdpObjectMapper ipaIdpObjectMapper = new KcIdpObjectMapper();
        User user = ipaIdpObjectMapper.mapUserMapToUser(userMap);

        assertEquals(userMap.get("username"), user.getUsername());
        assertEquals(userMap.get("firstName"), user.getFirstname());
        assertEquals(userMap.get("lastName"), user.getLastname());
        assertEquals(userMap.get("email"), user.getEmail());
    }

    @Test
    public void mapUsergroupMapToUsergroup_WhenGivenUsergroupMap_ShouldReturnUsergroup()
            throws Exception {
        Map<String, Object> usergroupMap = new HashMap<>();
        usergroupMap.put("name", "test-name");

        KcIdpObjectMapper ipaIdpObjectMapper = new KcIdpObjectMapper();
        Usergroup usergroup = ipaIdpObjectMapper.mapUsergroupMapToUsergroup(usergroupMap);

        assertEquals(usergroupMap.get("name"), usergroup.getName());
    }

    @Test
    public void buildUserCredentialsMap_WhenGivenPassword_ShouldReturnMapWithValueOfPassword()
            throws Exception {
        String password = "test-password";

        KcIdpObjectMapper ipaIdpObjectMapper = new KcIdpObjectMapper();
        assertEquals(password, ipaIdpObjectMapper.buildUserCredentialsMap(password).get("value"));
        assertEquals(false, ipaIdpObjectMapper.buildUserCredentialsMap(password).get("temporary"));
    }

    private User buildUserObject() {
        User user = new User();
        user.setUsername("test-username");
        user.setFirstname("test-firstname");
        user.setLastname("test-lastname");
        user.setPassword("test-password");
        user.setEmail("test-email@test.com");

        return user;
    }

    private Map<String, Object> buildUserMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", user.getUsername());
        userMap.put("firstName", user.getFirstname());
        userMap.put("lastName", user.getLastname());
        userMap.put("email", user.getEmail());

        return userMap;
    }
}
