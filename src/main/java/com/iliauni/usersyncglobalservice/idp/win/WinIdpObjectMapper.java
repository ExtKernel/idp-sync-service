package com.iliauni.usersyncglobalservice.idp.win;

import com.iliauni.usersyncglobalservice.idp.IdpObjectMapper;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class WinIdpObjectMapper implements IdpObjectMapper {
    @Override
    public Map<String, Object> mapUserToMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", user.getUsername());
        userMap.put("fullName", user.getFirstname() + " " + user.getLastname());
        userMap.put("password", user.getPassword());

        return userMap;
    }

    @Override
    public Map<String, Object> mapUsergroupToMap(Usergroup usergroup) {
        Map<String, Object> usergroupMap = new HashMap<>();
        usergroupMap.put("name", usergroup.getName());
        usergroupMap.put("description", usergroup.getDescription());
        usergroupMap.put("users", usergroup.getUsers());

        return usergroupMap;
    }

    @Override
    public User mapUserMapToUser(Map<String, Object> userMap) {
        User user = new User();
        user.setUsername((String) userMap.get("username"));

        return user;
    }

    @Override
    public Usergroup mapUsergroupMapToUsergroup(Map<String, Object> usergroupMap) {
        Usergroup usergroup = new Usergroup();
        usergroup.setName((String) usergroupMap.get("name"));
        usergroup.setDescription((String) usergroupMap.get("description"));

        // convert username list to a User list
        List<User> users = new ArrayList<>();
        for (String username : (List<String>) usergroupMap.get("users")) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("username", username);

            users.add(mapUserMapToUser(userMap));
        }
        usergroup.setUsers(users);

        return usergroup;
    }

    @Override
    public Map<String, Object> buildUserCredentialsMap(String password) {
        Map<String, Object> userCredentials = new HashMap<>();
        userCredentials.put("value", password);

        return userCredentials;
    }
}
