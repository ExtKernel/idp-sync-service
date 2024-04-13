package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class KcIdpObjectMapper implements IdpObjectMapper {

    @Override
    public MultiValueMap<String, Object> mapUserToMap(User user) {
        MultiValueMap<String, Object> userMap = new LinkedMultiValueMap<>();
        userMap.add("username", user.getUsername());
        userMap.add("firstName", user.getFirstname());
        userMap.add("lastName", user.getLastname());
        userMap.add("email", user.getEmail());
        userMap.add("credentials", buildUserCredentialsMap(user.getPassword()));

        return userMap;
    }

    @Override
    public MultiValueMap<String, Object> mapUsergroupToMap(Usergroup usergroup) {
        MultiValueMap<String, Object> usergroupMap = new LinkedMultiValueMap<>();
        usergroupMap.add("name", usergroup.getName());

        return usergroupMap;
    }

    @Override
    public User mapUserMapToUser(MultiValueMap<String, Object> userMap) {
        User user = new User();
        user.setUsername((String) userMap.get("username").get(0));
        user.setFirstname((String) userMap.get("firstName").get(0));
        user.setLastname((String) userMap.get("lastName").get(0));
        user.setEmail((String) userMap.get("email").get(0));

        return user;
    }

    @Override
    public Usergroup mapUsergroupMapToUsergroup(MultiValueMap<String, Object> usergroupMap) {
        Usergroup usergroup = new Usergroup();
        usergroup.setName((String) usergroupMap.get("name").get(0));

        return usergroup;
    }

    @Override
    public MultiValueMap<String, Object> buildUserCredentialsMap(String password) {
        MultiValueMap<String, Object> userCredentials = new LinkedMultiValueMap<>();
        userCredentials.add("value", password);
        userCredentials.add("temporary", false);

        return userCredentials;
    }
}
