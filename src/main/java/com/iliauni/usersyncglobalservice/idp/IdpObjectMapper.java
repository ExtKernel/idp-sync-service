package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * An interface for mapping objects to and from multi-value maps in an Identity Provider (IDP) context.
 */
public interface IdpObjectMapper {
    /**
     * Maps a {@link User} object to a multi-value map representation.
     *
     * @param user the user object to map
     * @return a multi-value map representation of the user
     */
    Map<String, Object> mapUserToMap(User user);

    /**
     * Maps a {@link Usergroup} object to a multi-value map representation.
     *
     * @param usergroup the user group object to map
     * @return a multi-value map representation of the user group
     */
    Map<String, Object> mapUsergroupToMap(Usergroup usergroup);

    /**
     * Maps a multi-value map representation of a user to a {@link User} object.
     *
     * @param userMap the multi-value map representation of the user
     * @return the user object
     */
    User mapUserMapToUser(Map<String, Object> userMap);

    /**
     * Maps a multi-value map representation of a user group to a {@link Usergroup} object.
     *
     * @param usergroupMap the multi-value map representation of the user group
     * @return the user group object
     */
    Usergroup mapUsergroupMapToUsergroup(Map<String, Object> usergroupMap);

    /**
     * Builds a multi-value map representing user credentials, typically including a password.
     *
     * @param password the password to include in the credentials map
     * @return a multi-value map representing user credentials
     */
    Map<String, Object> buildUserCredentialsMap(String password);
}
