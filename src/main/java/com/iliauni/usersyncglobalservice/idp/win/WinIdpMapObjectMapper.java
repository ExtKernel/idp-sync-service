package com.iliauni.usersyncglobalservice.idp.win;

import com.iliauni.usersyncglobalservice.idp.IdpMapObjectMapper;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WinIdpMapObjectMapper implements IdpMapObjectMapper {
    @Override
    public Map<String, Object> mapUserToMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", user.getUsername());
        userMap.put("fistName", user.getFirstname());
        userMap.put("lastName", user.getLastname());
        userMap.put("password", buildUserCredentialsMap(user.getPassword()).get("value"));

        return userMap;
    }

    @Override
    public Map<String, Object> mapUsergroupToMap(Usergroup usergroup) {
        Map<String, Object> usergroupMap = new HashMap<>();
        usergroupMap.put("name", usergroup.getName());
        usergroupMap.put("description", usergroup.getDescription());

        return usergroupMap;
    }

    @Override
    public Map<String, Object> buildUserCredentialsMap(String password) {
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("value", password);

        return credentials;
    }
}
