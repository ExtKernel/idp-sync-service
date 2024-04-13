package com.iliauni.usersyncglobalservice.unit.idp;

import com.iliauni.usersyncglobalservice.idp.IpaIdpObjectMapper;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IpaIdpObjectMapperTest {

    @Test
    public void mapUserToMap_WhenGivenUser_ShouldReturnMap()
            throws Exception {
        User user = buildUserObject();

        IpaIdpObjectMapper ipaIdpObjectMapper = new IpaIdpObjectMapper();
        MultiValueMap<String, Object> userMap = ipaIdpObjectMapper.mapUserToMap(user);

        assertEquals(user.getUsername(), userMap.get("uid").get(0));
        assertEquals(user.getFirstname(), userMap.get("givenname").get(0));
        assertEquals(user.getLastname(), userMap.get("sn").get(0));
        assertEquals(user.getEmail(), userMap.get("mail").get(0));
    }

    @Test
    public void mapUsergroupToMap_WhenGivenUsergroup_ShouldReturnMap()
            throws Exception {
        Usergroup usergroup = new Usergroup();
        usergroup.setName("test-name");
        usergroup.setDescription("test-description");

        IpaIdpObjectMapper ipaIdpObjectMapper = new IpaIdpObjectMapper();
        MultiValueMap<String, Object> usergroupMap = ipaIdpObjectMapper.mapUsergroupToMap(usergroup);

        assertEquals(usergroup.getName(), usergroupMap.get("cn").get(0));
        assertEquals(usergroup.getDescription(), usergroupMap.get("description").get(0));
    }

    @Test
    public void mapUserMapToUser_WhenGivenUserMap_ShouldReturnUser()
            throws Exception {
        MultiValueMap<String, Object> userMap = buildUserMap();

        IpaIdpObjectMapper ipaIdpObjectMapper = new IpaIdpObjectMapper();
        User user = ipaIdpObjectMapper.mapUserMapToUser(userMap);

        assertEquals(userMap.get("uid").get(0), user.getUsername());
        assertEquals(userMap.get("givenname").get(0), user.getFirstname());
        assertEquals(userMap.get("sn").get(0), user.getLastname());
        assertEquals(userMap.get("mail").get(0), user.getEmail());
    }

    @Test
    public void mapUsergroupMapToUsergroup_WhenGivenUsergroupMap_ShouldReturnUsergroup()
            throws Exception {
        MultiValueMap<String, Object> usergroupMap = new LinkedMultiValueMap<>();
        usergroupMap.add("cn", "test-name");
        usergroupMap.add("description", "test-description");

        IpaIdpObjectMapper ipaIdpObjectMapper = new IpaIdpObjectMapper();
        Usergroup usergroup = ipaIdpObjectMapper.mapUsergroupMapToUsergroup(usergroupMap);

        assertEquals(usergroupMap.get("cn").get(0), usergroup.getName());
        assertEquals(usergroupMap.get("description").get(0), usergroup.getDescription());
    }

    @Test
    public void buildUserCredentialsMap_WhenGivenPassword_ShouldReturnMapWithValueOfPassword()
            throws Exception {
        String password = "test-password";

        IpaIdpObjectMapper ipaIdpObjectMapper = new IpaIdpObjectMapper();
        assertEquals(password, ipaIdpObjectMapper.buildUserCredentialsMap(password).get("value").get(0));
    }

    private User buildUserObject() {
        User user = new User();
        user.setUsername("test-username");
        user.setFirstname("test-firstname");
        user.setLastname("test-lastname");
        user.setEmail("test-email@test.com");

        return user;
    }

    private MultiValueMap<String, Object> buildUserMap() {
        MultiValueMap<String, Object> userMap = new LinkedMultiValueMap<>();
        userMap.add("uid", "test-username");
        userMap.add("givenname", "test-firstname");
        userMap.add("sn", "test-lastname");
        userMap.add("mail", "test-email@test.com");

        return userMap;
    }
}
