package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.model.Client;
import com.iliauni.usersyncglobalservice.model.User;

import java.util.List;

/**
 * An interface to manage users in an Identity Provider (IDP) context, mainly using request senders and JSON object mappers
 *
 * @param <T> a type of client used for requests and mapping
 */
public interface IdpUserManager<T extends Client> {
    /**
     * Creates a user using the specified client and user object.
     *
     * @param client the client to perform a request on
     * @param user the user to be created
     * @param validate use internal validation, before sending any requests or not.
     * @return the created user
     */
    User createUser(
            T client,
            User user,
            boolean validate
    );

    /**
     * Retrieves user using the specified client and username.
     *
     * @param client the client to perform a request on
     * @param username username of the user
     * @param validate use internal validation, before sending any requests or not.
     * @return the retrieved user
     */
    User getUser(
            T client,
            String username,
            boolean validate
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
     * @param validate use internal validation, before sending any requests or not.
     * @return the updated password
     */
    String updateUserPassword(
            T client,
            String username,
            String newPassword,
            boolean validate
    );

    /**
     * Deletes a user using the specified client and username.
     *
     * @param client the client to perform a request on
     * @param username username of the user
     * @param validate use internal validation, before sending any requests or not.
     */
    void deleteUser(
            T client,
            String username,
            boolean validate
    );
}
