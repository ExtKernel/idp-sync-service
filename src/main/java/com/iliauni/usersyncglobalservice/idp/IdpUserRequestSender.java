package com.iliauni.usersyncglobalservice.idp;

import com.fasterxml.jackson.databind.JsonNode;
import com.iliauni.usersyncglobalservice.exception.CredentialsRepresentationBuildingException;
import com.iliauni.usersyncglobalservice.exception.GetUserRequestJsonReadingException;
import com.iliauni.usersyncglobalservice.exception.GetUsersRequestJsonReadingException;
import com.iliauni.usersyncglobalservice.exception.UserToJsonMappingException;
import com.iliauni.usersyncglobalservice.model.Client;
import com.iliauni.usersyncglobalservice.model.User;

/**
 * An interface for sending requests related to users in an Identity Provider (IDP) context.
 *
 * @param <T> the type of client used for the request
 */
public interface IdpUserRequestSender<T extends Client> {
    /**
     * Sends a request to create a user using the specified client.
     *
     * @param client the client used to create the user
     * @param user the user to be created
     * @return the created user
     * @throws UserToJsonMappingException throws if an exception occurred while mapping user object to a JSON string
     */
    User sendCreateUserRequest(
            T client,
            User user
    ) throws UserToJsonMappingException;

    /**
     * Sends a request to get a JSON of user with the specified username using the specified client.
     *
     * @param client the client used to retrieve the user
     * @param username the username of the user to retrieve
     * @return a JSON node containing retrieved user
     * @throws GetUserRequestJsonReadingException throws if an exception occurred while reading results of a user retrieving request
     */
    JsonNode sendGetUserRequest(
            T client,
            String username
    ) throws GetUserRequestJsonReadingException;

    /**
     * Sends a request to get a list of users using the specified client.
     *
     * @param client the client used to retrieve the users
     * @return a JSON node containing list of all users
     * @throws GetUsersRequestJsonReadingException throws if an exception occurred while reading results of users retrieving request
     */
    JsonNode sendGetUsersRequest(T client)
            throws GetUsersRequestJsonReadingException;

    /**
     * Sends a request to update the password of a user with the specified username using the specified client.
     *
     * @param client the client used to update the user password
     * @param username the username of the user whose password to update
     * @param newPassword the new password for the user
     * @return the updated password
     * @throws CredentialsRepresentationBuildingException throws if an exception occurred while building credentials representation with the given password
     */
    String sendUpdateUserPasswordRequest(
            T client,
            String username,
            String newPassword
    ) throws CredentialsRepresentationBuildingException;

    /**
     * Sends a request to delete a user with the specified username using the specified client.
     *
     * @param client the client used to delete the user
     * @param username the username of the user to delete
     */
    void sendDeleteUserRequest(
            T client,
            String username
    );
}
