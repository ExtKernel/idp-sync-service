package com.iliauni.idpsyncservice.kc;

import com.iliauni.idpsyncservice.idp.IdpMapObjectMapper;
import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.model.Usergroup;
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
        // add the necessary fields
        userMap.put("username", user.getUsername());
        userMap.put("enabled", true);

        // add optional fields
        // add user's first name, if not null
        String firstName = user.getFirstname();
        if (firstName != null) userMap.put("firstName", user.getFirstname());

        // add user's last name, if not null
        String lastName = user.getLastname();
        if (lastName != null) userMap.put("lastName", user.getLastname());

        // add user's email, if not null
        String email = user.getEmail();
        if (email != null) userMap.put("email", user.getEmail());

        // add user's password, if not null
        String password = user.getPassword();
        if (password != null) userMap.put("credentials", buildUserCredentialsMap(user.getPassword()));

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
