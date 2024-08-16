package com.iliauni.idpsyncservice.ipa;

import com.iliauni.idpsyncservice.idp.IdpMapObjectMapper;
import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.model.Usergroup;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Component;

/**
 * A component class implementing the {@link IdpMapObjectMapper} interface for mapping objects to maps in an Identity Provider (IDP) context specific to FreeIPA (Identity, Policy, and Audit) systems.
 * This implementation provides methods to map {@link User} and {@link Usergroup} objects to map representations, as well as methods for building user credentials maps.
 */
@Component
public class IpaIdpMapObjectMapper implements IdpMapObjectMapper {
    @Override
    public Map<String, Object> mapUserToMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        // add user's first name, if not null
        // if null, add a placeholder
        String firstName = user.getFirstname();
        userMap.put("givenname", Objects.requireNonNullElse(firstName, "placeholder"));

        // add user's last name, if not null
        // if null, add a placeholder
        String lastName = user.getLastname();
        userMap.put("sn", Objects.requireNonNullElse(lastName, "placeholder"));

        // add user's email, if not null
        // if null, add a placeholder
        String email = user.getEmail();
        userMap.put("mail", Objects.requireNonNullElse(email, "placeholder@placeholder.com"));

        userMap.put("cn", "users");

        return userMap;
    }

    @Override
    public Map<String, Object> mapUsergroupToMap(Usergroup usergroup) {
        Map<String, Object> usergroupMap = new HashMap<>();
        usergroupMap.put("description", usergroup.getDescription());

        return usergroupMap;
    }

    @Override
    public Map<String, Object> buildUserCredentialsMap(String password) {
        Map<String, Object> userCredentials = new HashMap<>();
        userCredentials.put("password", password);

        return userCredentials;
    }
}
