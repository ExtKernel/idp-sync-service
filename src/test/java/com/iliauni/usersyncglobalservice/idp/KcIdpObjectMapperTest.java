package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KcIdpObjectMapperTest {
    @Test
    public void mapUserToMap_WhenGivenUser_ShouldReturnMap()
            throws Exception {
        User user = buildUserObject();

        KcIdpObjectMapper ipaIdpObjectMapper = new KcIdpObjectMapper();
        MultiValueMap<String, Object> userMap = ipaIdpObjectMapper.mapUserToMap(user);

        assertEquals(user.getUsername(), userMap.get("username").get(0));
        assertEquals(user.getFirstname(), userMap.get("firstName").get(0));
        assertEquals(user.getLastname(), userMap.get("lastName").get(0));
        assertEquals(user.getEmail(), userMap.get("email").get(0));
        assertEquals(user.getPassword(), ((MultiValueMap<String, Object>) userMap.get("credentials").get(0)).get("value").get(0));
        assertEquals(false, ((MultiValueMap<String, Object>) userMap.get("credentials").get(0)).get("temporary").get(0));
    }

    @Test
    public void mapUsergroupToMap_WhenGivenUsergroup_ShouldReturnMap()
            throws Exception {
        Usergroup usergroup = new Usergroup();
        usergroup.setName("test-name");

        KcIdpObjectMapper ipaIdpObjectMapper = new KcIdpObjectMapper();
        MultiValueMap<String, Object> usergroupMap = ipaIdpObjectMapper.mapUsergroupToMap(usergroup);

        assertEquals(usergroup.getName(), usergroupMap.get("name").get(0));
    }

    @Test
    public void mapUserMapToUser_WhenGivenUserMap_ShouldReturnUser()
            throws Exception {
        MultiValueMap<String, Object> userMap = buildUserMap();

        KcIdpObjectMapper ipaIdpObjectMapper = new KcIdpObjectMapper();
        User user = ipaIdpObjectMapper.mapUserMapToUser(userMap);

        assertEquals(userMap.get("username").get(0), user.getUsername());
        assertEquals(userMap.get("firstName").get(0), user.getFirstname());
        assertEquals(userMap.get("lastName").get(0), user.getLastname());
        assertEquals(userMap.get("email").get(0), user.getEmail());
    }

    @Test
    public void mapUsergroupMapToUsergroup_WhenGivenUsergroupMap_ShouldReturnUsergroup()
            throws Exception {
        MultiValueMap<String, Object> usergroupMap = new LinkedMultiValueMap<>();
        usergroupMap.add("name", "test-name");

        KcIdpObjectMapper ipaIdpObjectMapper = new KcIdpObjectMapper();
        Usergroup usergroup = ipaIdpObjectMapper.mapUsergroupMapToUsergroup(usergroupMap);

        assertEquals(usergroupMap.get("name").get(0), usergroup.getName());
    }

    @Test
    public void buildUserCredentialsMap_WhenGivenPassword_ShouldReturnMapWithValueOfPassword()
            throws Exception {
        String password = "test-password";

        KcIdpObjectMapper ipaIdpObjectMapper = new KcIdpObjectMapper();
        assertEquals(password, ipaIdpObjectMapper.buildUserCredentialsMap(password).get("value").get(0));
        assertEquals(false, ipaIdpObjectMapper.buildUserCredentialsMap(password).get("temporary").get(0));
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

    private MultiValueMap<String, Object> buildUserMap() {
        MultiValueMap<String, Object> userMap = new LinkedMultiValueMap<>();
        userMap.add("username", "test-username");
        userMap.add("firstName", "test-firstname");
        userMap.add("lastName", "test-lastname");
        userMap.add("email", "test-email@test.com");

        return userMap;
    }
}
