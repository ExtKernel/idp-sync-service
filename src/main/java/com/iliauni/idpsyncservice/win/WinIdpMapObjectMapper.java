package com.iliauni.idpsyncservice.win;

import com.iliauni.idpsyncservice.idp.IdpMapObjectMapper;
import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.model.Usergroup;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WinIdpMapObjectMapper implements IdpMapObjectMapper {
    @Override
    public Map<String, Object> mapUserToMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", user.getUsername());

        // add user's first name, if not null
        String firstName = user.getFirstname();
        if (firstName != null) userMap.put("firstName", user.getFirstname());

        // add user's last name, if not null
        String lastName = user.getLastname();
        if (lastName != null) userMap.put("lastName", user.getLastname());

        // add user's password, if not null
        String password = user.getPassword();
        if (password != null) userMap.put("password", user.getPassword());

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
