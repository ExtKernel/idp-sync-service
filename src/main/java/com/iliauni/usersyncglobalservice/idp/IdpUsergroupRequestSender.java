package com.iliauni.usersyncglobalservice.idp;

import com.fasterxml.jackson.databind.JsonNode;
import com.iliauni.usersyncglobalservice.exception.UsergroupJsonReadingException;
import com.iliauni.usersyncglobalservice.exception.UsergroupMembersJsonReadingException;
import com.iliauni.usersyncglobalservice.exception.UsergroupsJsonReadingException;
import com.iliauni.usersyncglobalservice.model.Client;
import com.iliauni.usersyncglobalservice.model.Usergroup;

/**
 * An interface for sending requests related to user groups in an Identity Provider (IDP) context.
 *
 * @param <T> a type of client used for the request
 */
public interface IdpUsergroupRequestSender<T extends Client> {
    /**
     * Sends a request to create a user group using the specified client and user group object.
     *
     * @param client the client to use for the request.
     * @param usergroup the user group object to create.
     * @return the user group.
     */
    Usergroup sendCreateUsergroupRequest(
            T client,
            Usergroup usergroup
    );

    void sendAddUsergroupMemberRequest(
            T client,
            String usergroupName,
            String username
    );

    /**
     * Sends a request to get a JSON of a user group
     * with the specified name using the specified client.
     *
     * @param client the client to use for the request.
     * @param usergroupName the name of the user group to retrieve.
     * @return a JSON node containing retrieved user group.
     * @throws UsergroupJsonReadingException if there was a problem reading
     *                                       a user group JSON received from an IDP.
     */
    JsonNode sendGetUsergroupRequest(
            T client,
            String usergroupName
    );

    /**
     * Sends a request to get a list of user groups using the specified client.
     *
     * @param client a client to use for the request.
     * @return a JSON node containing list of all user groups.
     * @throws UsergroupsJsonReadingException if there was a problem reading
     *                                        user groups JSON received from an IDP.
     */
    JsonNode sendGetUsergroupsRequest(T client);

    /**
     * Sends a request to get a JSON of user group members
     * with the specified name using the specified client.
     *
     * @param client the client to use for the request.
     * @param usergroupName the name of the user group to retrieve.
     * @return a JSON node containing retrieved user group members.
     * @throws UsergroupMembersJsonReadingException if there was a problem reading
     *                                              user group members JSON received from an IDP.
     */
    JsonNode sendGetUsergroupMembersRequest(
            T client,
            String usergroupName
    );

    /**
     * Sends a request to delete a user group with the specified name using the specified client.
     *
     * @param client the client to use for the request.
     * @param usergroupName the name of the user group to delete.
     */
    void sendDeleteUsergroupRequest(
            T client,
            String usergroupName
    );

    void sendRemoveUsergroupMemberRequest(
            T client,
            String usergroupName,
            String username
    );
}
