package com.iliauni.usersyncglobalservice.idp.kc;

import com.iliauni.usersyncglobalservice.idp.IdpObjectMapper;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * A component class implementing the {@link IdpObjectMapper} interface for mapping objects to and from multi-value maps in an Identity Provider (IDP) context specific to Keycloak (KC).
 * This implementation provides methods to map {@link User} and {@link Usergroup} objects to and from multi-value map representations, as well as methods for building user credentials maps.
 */
@Component
public class KcIdpObjectMapper implements IdpObjectMapper {
    /**
     * @inheritDoc
     * Maps the given User object to the Keycloak-specific multi-value map format.
     * You can refer to Keycloak's documentation for UserRepresentation.
     */
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

    /**
     * @inheritDoc
     * Maps the given Usergroup object to the Keycloak-specific multi-value map format.
     * You can refer to Keycloak's documentation for GroupRepresentation.
     */
    @Override
    public Map<String, Object> mapUsergroupToMap(Usergroup usergroup) {
        Map<String, Object> usergroupMap = new HashMap<>();
        usergroupMap.put("name", usergroup.getName());

        return usergroupMap;
    }

    /**
     * @inheritDoc
     * Maps the multi-value map representation of a user to a User object.
     * You can refer to Keycloak's documentation for UserRepresentation.
     */
    @Override
    public User mapUserMapToUser(Map<String, Object> userMap) {
        User user = new User();
        user.setUsername((String) userMap.get("username"));
        user.setFirstname((String) userMap.get("firstName"));
        user.setLastname((String) userMap.get("lastName"));
        user.setEmail((String) userMap.get("email"));

        return user;
    }

    /**
     * @inheritDoc
     * Maps the multi-value map representation of a user group to a Usergroup object.
     * You can refer to Keycloak's documentation for GroupRepresentation.
     */
    @Override
    public Usergroup mapUsergroupMapToUsergroup(Map<String, Object> usergroupMap) {
        Usergroup usergroup = new Usergroup();
        usergroup.setName((String) usergroupMap.get("name"));

        return usergroup;
    }


    /**
     * @inheritDoc
     * Builds a multi-value map representing user credentials in the Keycloak-specific format.
     * You can refer to Keycloak's documentation for CredentialRepresentation.
     */
    @Override
    public Map<String, Object> buildUserCredentialsMap(String password) {
        Map<String, Object> userCredentials = new HashMap<>();
        userCredentials.put("value", password);
        userCredentials.put("temporary", false);

        return userCredentials;
    }
}
