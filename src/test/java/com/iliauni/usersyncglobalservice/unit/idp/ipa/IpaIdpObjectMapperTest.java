package com.iliauni.usersyncglobalservice.unit.idp.ipa;

import com.iliauni.usersyncglobalservice.idp.ipa.IpaIdpObjectMapper;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IpaIdpObjectMapperTest {

    @Test
    public void mapUserToMap_WhenGivenUser_ShouldReturnMap()
            throws Exception {
        User user = buildUserObject();

        IpaIdpObjectMapper ipaIdpObjectMapper = new IpaIdpObjectMapper();
        Map<String, Object> userMap = ipaIdpObjectMapper.mapUserToMap(user);

        assertEquals(user.getUsername(), userMap.get("uid"));
        assertEquals(user.getFirstname(), userMap.get("givenname"));
        assertEquals(user.getLastname(), userMap.get("sn"));
        assertEquals(user.getEmail(), userMap.get("mail"));
    }

    @Test
    public void mapUsergroupToMap_WhenGivenUsergroup_ShouldReturnMap()
            throws Exception {
        Usergroup usergroup = new Usergroup();
        usergroup.setName("test-name");
        usergroup.setDescription("test-description");

        IpaIdpObjectMapper ipaIdpObjectMapper = new IpaIdpObjectMapper();
        Map<String, Object> usergroupMap = ipaIdpObjectMapper.mapUsergroupToMap(usergroup);

        assertEquals(usergroup.getName(), usergroupMap.get("cn"));
        assertEquals(usergroup.getDescription(), usergroupMap.get("description"));
    }

    @Test
    public void mapUserMapToUser_WhenGivenUserMap_ShouldReturnUser()
            throws Exception {
        Map<String, Object> userMap = buildUserMap();

        IpaIdpObjectMapper ipaIdpObjectMapper = new IpaIdpObjectMapper();
        User user = ipaIdpObjectMapper.mapUserMapToUser(userMap);

        assertEquals(userMap.get("uid"), user.getUsername());
        assertEquals(userMap.get("givenname"), user.getFirstname());
        assertEquals(userMap.get("sn"), user.getLastname());
        assertEquals(userMap.get("mail"), user.getEmail());
    }

    @Test
    public void mapUsergroupMapToUsergroup_WhenGivenUsergroupMap_ShouldReturnUsergroup()
            throws Exception {
        Map<String, Object> usergroupMap = new HashMap<>();
        usergroupMap.put("cn", "test-name");
        usergroupMap.put("description", "test-description");

        IpaIdpObjectMapper ipaIdpObjectMapper = new IpaIdpObjectMapper();
        Usergroup usergroup = ipaIdpObjectMapper.mapUsergroupMapToUsergroup(usergroupMap);

        assertEquals(usergroupMap.get("cn"), usergroup.getName());
        assertEquals(usergroupMap.get("description"), usergroup.getDescription());
    }

    @Test
    public void buildUserCredentialsMap_WhenGivenPassword_ShouldReturnMapWithValueOfPassword()
            throws Exception {
        String password = "test-password";

        IpaIdpObjectMapper ipaIdpObjectMapper = new IpaIdpObjectMapper();
        assertEquals(password, ipaIdpObjectMapper.buildUserCredentialsMap(password).get("value"));
    }

    private User buildUserObject() {
        User user = new User();
        user.setUsername("test-username");
        user.setFirstname("test-firstname");
        user.setLastname("test-lastname");
        user.setEmail("test-email@test.com");

        return user;
    }

    private Map<String, Object> buildUserMap() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("uid", "test-username");
        userMap.put("givenname", "test-firstname");
        userMap.put("sn", "test-lastname");
        userMap.put("mail", "test-email@test.com");

        return userMap;
    }
}
