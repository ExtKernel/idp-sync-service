package com.iliauni.usersyncglobalservice.idp.ipa;

import com.iliauni.usersyncglobalservice.idp.IdpObjectMapper;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * A component class implementing the {@link IdpObjectMapper} interface for mapping objects to and from multi-value maps in an Identity Provider (IDP) context specific to FreeIPA (Identity, Policy, and Audit) systems.
 * This implementation provides methods to map {@link User} and {@link Usergroup} objects to and from multi-value map representations, as well as methods for building user credentials maps.
 */
@Component
public class IpaIdpObjectMapper implements IdpObjectMapper {
    /**
     * @inheritDoc
     * Maps the given User object to the IPA-specific multi-value map format.
     */
    @Override
    public Map<String, Object> mapUserToMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("uid", user.getUsername());
        userMap.put("givenname", user.getFirstname());
        userMap.put("sn", user.getLastname());
        userMap.put("mail", user.getEmail());
        userMap.put("cn", "users");

        return userMap;
    }

    /**
     * @inheritDoc
     * Maps the given Usergroup object to the IPA-specific multi-value map format.
     */
    @Override
    public Map<String, Object> mapUsergroupToMap(Usergroup usergroup) {
        Map<String, Object> usergroupMap = new HashMap<>();
        usergroupMap.put("cn", usergroup.getName());
        usergroupMap.put("description", usergroup.getDescription());

        return usergroupMap;
    }

    /**
     * @inheritDoc
     * Maps the multi-value map representation of a user to a User object.
     */
    @Override
    public User mapUserMapToUser(Map<String, Object> userMap) {
        User user = new User();
        user.setUsername(((String) userMap.get("uid")));
        user.setFirstname(((String) userMap.getOrDefault("givenname", null)));
        user.setLastname(((String)userMap.getOrDefault("sn", null)));
        user.setEmail(((String)userMap.getOrDefault("mail", null)));

        return user;
    }

    /**
     * @inheritDoc
     * Maps the multi-value map representation of a user group to an Usergroup object.
     */
    @Override
    public Usergroup mapUsergroupMapToUsergroup(Map<String, Object> usergroupMap) {
        Usergroup usergroup = new Usergroup();
        usergroup.setName(((String) usergroupMap.get("cn")));
        usergroup.setDescription(((String) usergroupMap.get("description")));

        return usergroup;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Map<String, Object> buildUserCredentialsMap(String password) {
        Map<String, Object> userCredentials = new HashMap<>();
        userCredentials.put("value", password);

        return userCredentials;
    }
}
