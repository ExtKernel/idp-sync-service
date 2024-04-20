package com.iliauni.usersyncglobalservice.unit.idp.win;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.iliauni.usersyncglobalservice.idp.win.WinIdpObjectMapper;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class WinIdpObjectMapperTest {
    @Test
    public void mapUserToMap_WhenGivenUser_ShouldReturnMap()
            throws Exception {
        User user = buildUserObject();

        WinIdpObjectMapper winIdpObjectMapper = new WinIdpObjectMapper();
        Map<String, Object> userMap = winIdpObjectMapper.mapUserToMap(user);

        assertEquals(user.getUsername(), userMap.get("username"));
        assertEquals(user.getFirstname(), userMap.get("fullName").toString().split(" ")[0]);
        assertEquals(user.getLastname(), userMap.get("fullName").toString().split(" ")[1]);
        assertEquals(user.getPassword(), userMap.get("password"));
    }

    @Test
    public void mapUsergroupToMap_WhenGivenUsergroup_ShouldReturnMap()
            throws Exception {
        Usergroup usergroup = buildUsergroupObject();

        WinIdpObjectMapper winIdpObjectMapper = new WinIdpObjectMapper();
        Map<String, Object> usergroupMap = winIdpObjectMapper.mapUsergroupToMap(usergroup);

        assertEquals(usergroup.getName(), usergroupMap.get("name"));
        assertEquals(usergroup.getDescription(), usergroupMap.get("description"));
        assertEquals(usergroup.getUsers(), usergroupMap.get("users"));
    }

    @Test
    public void mapUserMapToUser_WhenGivenUserMap_ShouldReturnUser()
            throws Exception {
        Map<String, Object> userMap = buildUserMap(buildUserObject());

        WinIdpObjectMapper winIdpObjectMapper = new WinIdpObjectMapper();
        assertEquals(userMap.get("username"), winIdpObjectMapper.mapUserMapToUser(userMap).getUsername());
    }

    @Test
    public void mapUsergroupMapToUsergroup_WhenGivenUsergroupMap_ShouldReturnUsergroup()
            throws Exception {
        Map<String, Object> usergroupMap = buildUsergroupMap();

        WinIdpObjectMapper winIdpObjectMapper = new WinIdpObjectMapper();
        Usergroup usergroup = winIdpObjectMapper.mapUsergroupMapToUsergroup(usergroupMap);

        assertEquals(usergroupMap.get("name"), usergroup.getName());
        assertEquals(usergroupMap.get("description"), usergroup.getDescription());
        assertEquals(((List<String>) usergroupMap.get("users")).get(0), usergroup.getUsers().get(0).getUsername());
    }

    @Test
    public void buildUserCredentialsMap_WhenGivenPassword_ShouldReturnMapWithValueOfPassword()
            throws Exception {
        String password = "test-password";

        WinIdpObjectMapper winIdpObjectMapper = new WinIdpObjectMapper();
        assertEquals(password, winIdpObjectMapper.buildUserCredentialsMap(password).get("value"));
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

    private Usergroup buildUsergroupObject() {
        Usergroup usergroup = new Usergroup();
        usergroup.setName("test-name");
        usergroup.setDescription("test-description");

        List<User> users = new ArrayList<>();
        users.add(buildUserObject());

        usergroup.setUsers(users);

        return usergroup;
    }

    private Map<String, Object> buildUserMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", user.getUsername());
        userMap.put("firstName", user.getFirstname());
        userMap.put("lastName", user.getLastname());
        userMap.put("email", user.getEmail());

        return userMap;
    }

    private Map<String, Object> buildUsergroupMap() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", "test-name");
        userMap.put("description", "test-description");

        List<String> usernames = new ArrayList<>();
        usernames.add(buildUserObject().getUsername());

        userMap.put("users", usernames);

        return userMap;
    }
}
