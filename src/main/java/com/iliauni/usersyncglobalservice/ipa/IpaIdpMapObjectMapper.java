package com.iliauni.usersyncglobalservice.ipa;

import com.iliauni.usersyncglobalservice.idp.IdpMapObjectMapper;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * A component class implementing the {@link IdpMapObjectMapper} interface for mapping objects to maps in an Identity Provider (IDP) context specific to FreeIPA (Identity, Policy, and Audit) systems.
 * This implementation provides methods to map {@link User} and {@link Usergroup} objects to map representations, as well as methods for building user credentials maps.
 */
@Component
public class IpaIdpMapObjectMapper implements IdpMapObjectMapper {
    @Override
    public Map<String, Object> mapUserToMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("givenname", user.getFirstname());
        userMap.put("sn", user.getLastname());
        userMap.put("mail", user.getEmail());
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
        userCredentials.put("value", password);

        return userCredentials;
    }
}
