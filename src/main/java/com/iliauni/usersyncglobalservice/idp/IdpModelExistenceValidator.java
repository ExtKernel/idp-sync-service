package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.exception.UsergroupDoesNotExistOnTheClientException;
import com.iliauni.usersyncglobalservice.model.Client;

/**
 * An interface to validate model object existence on an Identity Provider (IDP) client.
 *
 * @param <T> the IDP client
 */
public interface IdpModelExistenceValidator<T extends Client> {
    /**
     * Validates that user group exists on the IDP client.
     *
     * @param client the client to get user groups from.
     * @param usergroupName the name of the user group whose existence should be validated.
     * @return true, if the user group exists on the client. False, if it doesn't.
     */
    boolean validateUsergroupExistence(
            T client,
            String usergroupName
    );

    /**
     * Validates that user exists on the IDP client.
     *
     * @param client the client to get users from.
     * @param username the username of the user whose existence should be validated.
     * @return true, if the user exists on the client. False, if it doesn't.
     */
    boolean validateUserExistence(
            T client,
            String username
    );

    /**
     * Validates that user exists in the provided user group on the IDP client.
     *
     * @param client the client to get user group from.
     * @param usergroupName the name of the user group.
     * @param username the name of the user.
     * @return true, if the user exists in the user group. False, if it doesn't.
     * @throws UsergroupDoesNotExistOnTheClientException if the user group doesn't exist on the client.
     */
    boolean validateUsergroupUserExistence(
            T client,
            String usergroupName,
            String username
    ) throws UsergroupDoesNotExistOnTheClientException;
}
