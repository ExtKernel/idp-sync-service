package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.model.Client;
import com.iliauni.usersyncglobalservice.model.User;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

/**
 * An interface for sending requests related to users in an Identity Provider (IDP) context.
 *
 * @param <T> the type of client used for the request
 */
public interface IdpUserRequestSender<T extends Client> {

    /**
     * Creates a user using the specified client.
     *
     * @param client the client used to create the user
     * @param user the user to be created
     * @return the created user
     */
    User createUser(T client, User user);

    /**
     * Retrieves a user with the specified username using the specified client.
     *
     * @param client the client used to retrieve the user
     * @param username the username of the user to retrieve
     * @return the retrieved user
     */
    User getUser(T client, String username);

    /**
     * Retrieves a list of users using the specified client.
     *
     * @param client the client used to retrieve the users
     * @return a list of retrieved users
     */
    List<User> getUsers(T client);

    /**
     * Updates the password of a user with the specified username using the specified client.
     *
     * @param client the client used to update the user password
     * @param username the username of the user whose password to update
     * @param newPassword the new password for the user
     * @return a multi-value map representing the updated user's password information
     */
    Map<String, Object> updateUserPassword(
            T client,
            String username,
            String newPassword
    );

    /**
     * Deletes a user with the specified username using the specified client.
     *
     * @param client the client used to delete the user
     * @param username the username of the user to delete
     */
    void deleteUser(T client, String username);
}
