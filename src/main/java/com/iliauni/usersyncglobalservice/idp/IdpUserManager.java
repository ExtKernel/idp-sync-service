package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.model.Client;
import com.iliauni.usersyncglobalservice.model.User;

import java.util.List;

/**
 * An interface to manage users in an Identity Provider (IDP) context, mainly using request senders and JSON object mappers
 *
 * @param <T> the type of client used for requests and mapping
 */
public interface IdpUserManager<T extends Client> {
    /**
     * Creates a user using the specified client and user object.
     *
     * @param client the client to perform a request on
     * @param user the user to be created
     * @return the created user
     */
    User createUser(
            T client,
            User user
    );

    /**
     * Retrieves user using the specified client and username.
     *
     * @param client the client to perform a request on
     * @param username username of the user
     * @return the retrieved user
     */
    User getUser(
            T client,
            String username
    );

    /**
     * Retrieves users using the specified client.
     *
     * @param client the client to perform a request on
     * @return a list of retrieved users
     */
    List<User> getUsers(T client);

    /**
     * Updates user's password using the specified client, username and new password.
     *
     * @param client the client to perform a request on
     * @param username username of the user
     * @param newPassword the new password to set
     * @return the updated password
     */
    String updateUserPassword(
            T client,
            String username,
            String newPassword
    );

    /**
     * Deletes a user using the specified client and username.
     *
     * @param client the client to perform a request on
     * @param username username of the user
     */
    void deleteUser(
            T client,
            String username
    );
}
