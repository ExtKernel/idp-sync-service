package com.iliauni.idpsyncservice.idp;

import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.model.Usergroup;
import java.util.Map;

/**
 * An interface for mapping objects to maps in an Identity Provider (IDP) context.
 */
public interface IdpMapObjectMapper {
    /**
     * Maps a {@link User} object to a map representation.
     *
     * @param user the user object to map
     * @return a map representation of the user
     */
    Map<String, Object> mapUserToMap(User user);

    /**
     * Maps a {@link Usergroup} object to a map representation.
     *
     * @param usergroup the user group object to map
     * @return a map representation of the user group
     */
    Map<String, Object> mapUsergroupToMap(Usergroup usergroup);

    /**
     * Builds a map representing user credentials, typically including a password.
     *
     * @param password the password to include in the credentials map
     * @return a map representing user credentials
     */
    Map<String, Object> buildUserCredentialsMap(String password);
}
