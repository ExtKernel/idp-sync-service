package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;

@Component
public class IpaIdpObjectMapper implements IdpObjectMapper {

    @Override
    public MultiValueMap<String, Object> mapUserToMap(User user) {
        MultiValueMap<String, Object> userMap = new LinkedMultiValueMap<>();
        userMap.add("uid", user.getUsername());
        userMap.add("givenname", user.getFirstname());
        userMap.add("sn", user.getLastname());
        userMap.add("mail", user.getEmail());
        userMap.add("cn", "users");

        return userMap;
    }

    @Override
    public MultiValueMap<String, Object> mapUsergroupToMap(Usergroup usergroup) {
        MultiValueMap<String, Object> usergroupMap = new LinkedMultiValueMap<>();
        usergroupMap.add("cn", usergroup.getName());
        usergroupMap.add("description", usergroup.getDescription());

        return usergroupMap;
    }

    @Override
    public User mapUserMapToUser(MultiValueMap<String, Object> userMap) {
        User user = new User();
        user.setUsername(((ArrayList<?>) userMap.get("uid")).get(0).toString());
        user.setFirstname(((ArrayList<?>) userMap.getOrDefault("givenname", null)).get(0).toString());
        user.setLastname(((ArrayList<?>)userMap.getOrDefault("sn", null)).get(0).toString());
        user.setEmail(((ArrayList<?>)userMap.getOrDefault("mail", null)).get(0).toString());

        return user;
    }

    @Override
    public Usergroup mapUsergroupMapToUsergroup(MultiValueMap<String, Object> usergroupMap) {
        Usergroup usergroup = new Usergroup();
        usergroup.setName(((ArrayList<?>) usergroupMap.get("cn")).get(0).toString());
        usergroup.setDescription(((ArrayList<?>) usergroupMap.get("description")).get(0).toString());

        return usergroup;
    }

    @Override
    public MultiValueMap<String, Object> buildUserCredentialsMap(String password) {
        MultiValueMap<String, Object> userCredentials = new LinkedMultiValueMap<>();
        userCredentials.add("value", password);

        return userCredentials;
    }
}
