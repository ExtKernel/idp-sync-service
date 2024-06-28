package com.iliauni.usersyncglobalservice.kc;

import com.iliauni.usersyncglobalservice.idp.IdpMapObjectMapper;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * A component class implementing the {@link IdpMapObjectMapper} interface for mapping objects to maps in an Identity Provider (IDP) context specific to Keycloak (KC).
 * This implementation provides methods to map {@link User} and {@link Usergroup} objects to map representations, as well as methods for building user credentials maps.
 */
@Component
public class SyncKcIdpMapObjectMapper implements IdpMapObjectMapper {
    @Override
    public Map<String, Object> mapUserToMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", user.getUsername());
        userMap.put("firstName", user.getFirstname());
        userMap.put("lastName", user.getLastname());
        userMap.put("email", user.getEmail());
        userMap.put("credentials", buildUserCredentialsMap(user.getPassword()));

        return userMap;
    }

    @Override
    public Map<String, Object> mapUsergroupToMap(Usergroup usergroup) {
        Map<String, Object> usergroupMap = new HashMap<>();
        usergroupMap.put("name", usergroup.getName());

        return usergroupMap;
    }

    @Override
    public Map<String, Object> buildUserCredentialsMap(String password) {
        Map<String, Object> userCredentials = new HashMap<>();
        userCredentials.put("value", password);
        userCredentials.put("temporary", false);

        return userCredentials;
    }
}
