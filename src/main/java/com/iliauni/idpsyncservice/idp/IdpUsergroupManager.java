package com.iliauni.idpsyncservice.idp;

import com.iliauni.idpsyncservice.exception.UsergroupAlreadyExistsOnTheClientException;
import com.iliauni.idpsyncservice.exception.UsergroupDoesNotExistOnTheClientException;
import com.iliauni.idpsyncservice.exception.UsergroupMemberAlreadyExistsOnTheClientException;
import com.iliauni.idpsyncservice.model.Client;
import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.model.Usergroup;

import java.util.List;

/**
 * An interface to manage user groups in an Identity Provider (IDP) context,
 * Mainly using request senders and JSON object mappers.
 * Mostly, it servers as an abstraction and connection point for request senders and object mappers
 *
 * @param <T> a type of client used for requests and mapping
 */
public interface IdpUsergroupManager<T extends Client> {
    /**
     * Creates a user group using the specified client and usergroup object.
     *
     * @param client the client to perform the request on.
     * @param usergroup the user group to be created.
     * @param validate use internal validation, before sending any requests or not.
     * @return the created user group.
     * @throws UsergroupAlreadyExistsOnTheClientException
     */
    Usergroup createUsergroup(
            T client,
            Usergroup usergroup,
            boolean validate
    );

    /**
     * Adds a user to a user group using the specified
     * client, name of the user group and username of the user to be added.
     *
     * @param client the client to perform the request on.
     * @param usergroupName name of the user group.
     * @param username username of the user to be added.
     * @param validate use internal validation, before sending any requests or not.
     * @throws UsergroupMemberAlreadyExistsOnTheClientException if the user is already
     *                                                       a member of the user group.
     * @throws UsergroupDoesNotExistOnTheClientException
     */
    void addUsergroupMember(
            T client,
            String usergroupName,
            String username,
            boolean validate
    );

    /**
     * Retrieves user group using the specified client and name of the user group.
     *
     * @param client the client to perform the request on
     * @param usergroupName name of the user group
     * @param validate use internal validation, before sending any requests or not.
     * @return the retrieved user
     * @throws UsergroupDoesNotExistOnTheClientException
     */
    Usergroup getUsergroup(
            T client,
            String usergroupName,
            boolean validate
    );

    /**
     * Retrieves user groups using the specified client.
     *
     * @param client the client to perform the request on
     * @return a list of retrieved user groups
     */
    List<Usergroup> getUsergroups(T client);

    /**
     * Retrieves members of a user group using the specified client and name of the user group.
     *
     * @param client the client to perform a request on
     * @param usergroupName name of the user group
     * @param validate use internal validation, before sending any requests or not.
     * @return a list of members of the user group
     * @throws UsergroupDoesNotExistOnTheClientException
     */
    List<User> getUsergroupMembers(
            T client,
            String usergroupName,
            boolean validate
    );

    /**
     * Deletes a user group using the specified client and name of the user group.
     *
     * @param client the client to perform the request on.
     * @param usergroupName name of the user group.
     * @param validate use internal validation, before sending any requests or not.
     * @throws UsergroupDoesNotExistOnTheClientException
     */
    void deleteUsergroup(
            T client,
            String usergroupName,
            boolean validate
    );

    /**
     * Removes users from a user group using the specified
     * client, name of the user group and username of the user to be removed.
     *
     * @param client the client to perform the request on.
     * @param usergroupName name of the user group.
     * @param username username of the user to be removed.
     */
    void removeUsergroupMember(
            T client,
            String usergroupName,
            String username,
            boolean validate
    );
}
