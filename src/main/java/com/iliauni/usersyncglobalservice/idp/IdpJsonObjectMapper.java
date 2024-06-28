package com.iliauni.usersyncglobalservice.idp;

import com.fasterxml.jackson.databind.JsonNode;
import com.iliauni.usersyncglobalservice.exception.UserToJsonMappingException;
import com.iliauni.usersyncglobalservice.exception.UsergroupToJsonMappingException;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;

/**
 * An interface for mapping objects to and from JSON in an Identity Provider (IDP) context.
 */
public interface IdpJsonObjectMapper {
    /**
     * Maps a {@link User} object to a JSON string representation.
     *
     * @param user the user object to map.
     * @return a JSON string representation of the user.
     * @throws UserToJsonMappingException if there was a problem while writing
     *                                         a user object as a JSON.
     */
    String mapUserToJsonString(User user);

    /**
     * Maps a {@link Usergroup} object to a JSON string representation.
     *
     * @param usergroup the user group object to map
     * @return a JSON string representation of the user group
     * @throws UsergroupToJsonMappingException if there was a problem while writing
     *                                         a user group object as a JSON.
     */
    String mapUsergroupToJsonString(Usergroup usergroup)
    ;

    /**
     * Builds JSON credentials representation and converts into a string.
     *
     * @param password the password to include in the representation.
     * @return a JSON string credentials representation.
     */
    String buildCredentialsRepresentation(String password);

    /**
     * Maps a JSON node representation of a user to a {@link User} object.
     *
     * @param userJsonNode the JSON node representation of the user.
     * @return the user object.
     */
    User mapUserJsonNodeToUser(JsonNode userJsonNode);

    /**
     * Maps a JSON node representation of a user group to a {@link Usergroup} object.
     *
     * @param usergroupJsonNode the JSON node representation of the user group.
     * @return the user group object.
     */
    Usergroup mapUsergroupJsonNodeToUsergroup(JsonNode usergroupJsonNode);
}
