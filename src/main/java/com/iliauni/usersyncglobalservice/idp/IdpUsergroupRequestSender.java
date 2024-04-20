package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.model.Client;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;

import java.util.List;

/**
 * An interface for sending requests related to user groups in an Identity Provider (IDP) context.
 *
 * @param <T> the type of client used for the request
 */
public interface IdpUsergroupRequestSender<T extends Client> {
    /**
     * Creates a user group using the specified client and user group object.
     *
     * @param client the client to use for the request
     * @param usergroup the user group object to create
     * @return the created user group
     */
    Usergroup createUsergroup(T client, Usergroup usergroup);

    /**
     * Retrieves a user group with the specified name using the specified client.
     *
     * @param client the client to use for the request
     * @param usergroupName the name of the user group to retrieve
     * @return the retrieved user group
     */
    Usergroup getUsergroup(T client, String usergroupName);

    /**
     * Retrieves all user groups using the specified client.
     *
     * @param client the client to use for the request
     * @return a list of all user groups
     */
    List<Usergroup> getUsergroups(T client);

    /**
     * Deletes a user group with the specified name using the specified client.
     *
     * @param client the client to use for the request
     * @param usergroupName the name of the user group to delete
     */
    void deleteUsergroup(T client, String usergroupName);
}
