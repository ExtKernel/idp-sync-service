package com.iliauni.usersyncglobalservice.idp;

import com.fasterxml.jackson.databind.JsonNode;
import com.iliauni.usersyncglobalservice.exception.UserJsonReadingException;
import com.iliauni.usersyncglobalservice.exception.UsersJsonReadingException;
import com.iliauni.usersyncglobalservice.model.Client;
import com.iliauni.usersyncglobalservice.model.User;

/**
 * An interface for sending requests related to users in an Identity Provider (IDP) context.
 *
 * @param <T> a type of client used for the request
 */
public interface IdpUserRequestSender<T extends Client> {
    /**
     * Sends a request to create a user using the specified client.
     *
     * @param client the client used to create the user
     * @param user the user to be created
     * @return the created user
     */
    User sendCreateUserRequest(
            T client,
            User user
    );

    /**
     * Sends a request to get a JSON of user with the specified username using the specified client.
     *
     * @param client the client used to retrieve the user
     * @param username the username of the user to retrieve
     * @return a JSON node containing retrieved user
     * @throws UserJsonReadingException if there was a problem reading a user JSON received from an IDP.
     */
    JsonNode sendGetUserRequest(
            T client,
            String username
    );

    /**
     * Sends a request to get a list of users using the specified client.
     *
     * @param client the client used to retrieve the users
     * @return a JSON node containing list of all users
     * @throws UsersJsonReadingException if there was a problem reading users JSON received from an IDP.
     */
    JsonNode sendGetUsersRequest(T client);

    /**
     * Sends a request to update the password of a user with the specified username using the specified client.
     *
     * @param client the client used to update the user password
     * @param username the username of the user whose password to update
     * @param newPassword the new password for the user
     * @return the updated password
     */
    String sendUpdateUserPasswordRequest(
            T client,
            String username,
            String newPassword
    );

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
