package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.exception.UsergroupAlreadyExistsOnTheClientException;
import com.iliauni.usersyncglobalservice.exception.UsergroupDoesNotExistOnTheClientException;
import com.iliauni.usersyncglobalservice.exception.UsergroupUserAlreadyExistsOnTheClientException;
import com.iliauni.usersyncglobalservice.model.Client;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;

import java.util.List;

/**
 * An interface to manage user groups in an Identity Provider (IDP) context,
 * Mainly using request senders and JSON object mappers.
 * Mostly, it servers as an abstraction and connection point for request senders and object mappers
 *
 * @param <T> the type of client used for requests and mapping
 */
public interface IdpUsergroupManager<T extends Client> {
    /**
     * Creates a user group using the specified client and usergroup object.
     *
     * @param client the client to perform a request on
     * @param usergroup the user group to be created
     * @return the created user group
     */
    Usergroup createUsergroup(
            T client,
            Usergroup usergroup
    ) throws UsergroupAlreadyExistsOnTheClientException;

    /**
     * Adds a user to a user group using the specified
     * client, name of the user group and username of the user to be added.
     *
     * @param client the client to perform a request on.
     * @param usergroupName name of the user group.
     * @param username username of the user to be added.
     * @throws UsergroupUserAlreadyExistsOnTheClientException if the user is already
     *                                                       a member of the user group.
     */
    void addUsergroupMember(
            T client,
            String usergroupName,
            String username
    ) throws UsergroupUserAlreadyExistsOnTheClientException;

    /**
     * Retrieves user group using the specified client and name of the user group.
     *
     * @param client the client to perform a request on
     * @param usergroupName name of the user group
     * @return the retrieved user
     */
    Usergroup getUsergroup(
            T client,
            String usergroupName
    );

    /**
     * Retrieves user groups using the specified client.
     *
     * @param client the client to perform a request on
     * @return a list of retrieved user groups
     */
    List<Usergroup> getUsergroups(T client);

    /**
     * Retrieves members of a user group using the specified client and name of the user group.
     *
     * @param client the client to perform a request on
     * @param usergroupName name of the user group
     * @return a list of members of the user group
     */
    List<User> getUsergroupMembers(
            T client,
            String usergroupName
    );

    /**
     * Deletes a user group using the specified client and name of the user group.
     *
     * @param client the client to perform a request on
     * @param usergroupName name of the user group
     */
    void deleteUsergroup(
            T client,
            String usergroupName
    ) throws UsergroupDoesNotExistOnTheClientException;

    /**
     * Removes users from a user group using the specified
     * client, name of the user group and username of the user to be removed.
     *
     * @param client the client to perform a request on
     * @param usergroupName name of the user group
     * @param username username of the user to be removed
     */
    void removeUsergroupMember(
            T client,
            String usergroupName,
            String username
    );
}
