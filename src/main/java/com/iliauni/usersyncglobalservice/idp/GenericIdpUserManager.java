package com.iliauni.usersyncglobalservice.idp;

import com.fasterxml.jackson.databind.JsonNode;
import com.iliauni.usersyncglobalservice.exception.UserAlreadyExistsOnTheClientException;
import com.iliauni.usersyncglobalservice.exception.UserDoesNotExistOnTheClientException;
import com.iliauni.usersyncglobalservice.model.Client;
import com.iliauni.usersyncglobalservice.model.User;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericIdpUserManager<T extends Client> implements IdpUserManager<T> {

    @Getter(AccessLevel.PROTECTED)
    private final IdpJsonObjectMapper jsonObjectMapper;

    @Getter(AccessLevel.PROTECTED)
    private final IdpUserRequestSender<T> requestSender;

    @Getter(AccessLevel.PROTECTED)
    private final IdpModelExistenceValidator<T> modelExistenceValidator;

    @Getter(AccessLevel.PROTECTED)
    private final UserIdpRequestSenderResultBlackListFilter<T> blackListFilter;

    public GenericIdpUserManager(
            IdpJsonObjectMapper jsonObjectMapper,
            IdpUserRequestSender<T> requestSender,
            IdpModelExistenceValidator<T> modelExistenceValidator,
            UserIdpRequestSenderResultBlackListFilter<T> blackListFilter
    ) {
        this.jsonObjectMapper = jsonObjectMapper;
        this.requestSender = requestSender;
        this.modelExistenceValidator = modelExistenceValidator;
        this.blackListFilter = blackListFilter;
    }

    /**
     * @param validate validate that the user doesn't exist
     *                before sending a request to create or not.
     */
    @Override
    public User createUser(
            T client,
            User user,
            boolean validate
    ) {
        try {
            validateUserDoesNotExists(
                    client,
                    user.getUsername()
            );
        } catch (UserAlreadyExistsOnTheClientException exception) {
            return null;
        }


        return requestSender.sendCreateUserRequest(
                client,
                user
        );
    }

    /**
     * @param validate validate that the user exists
     *                before sending a request to get it or not.
     */
    @Override
    public User getUser(
            T client,
            String username,
            boolean validate
    ) {
        validateUserExists(
                client,
                username
        );

        return blackListFilter.filter(
                client,
                jsonObjectMapper.mapUserJsonNodeToUser(
                        requestSender.sendGetUserRequest(
                                client,
                                username
                        ))
        );
    }

    @Override
    public List<User> getUsers(T client) {
        List<User> users = new ArrayList<>();

        // iterate over JSON nodes which represent users and map each one to an object
        for (JsonNode user : requestSender.sendGetUsersRequest(client)) {
            users.add(jsonObjectMapper.mapUserJsonNodeToUser(user));
        }

        return blackListFilter.filter(
                client,
                users
        );
    }

    /**
     * @param validate validate that the user group exists
     *                before sending a request to update its password or not.
     */
    @Override
    public String updateUserPassword(
            T client,
            String username,
            String newPassword,
            boolean validate
    ) {
        validateUserExists(
                client,
                username
        );

        return requestSender.sendUpdateUserPasswordRequest(
                client,
                username,
                newPassword
        );
    }

    /**
     * @param validate validate that the user group exists
     *                before sending a request to delete it or not.
     */
    @Override
    public void deleteUser(
            T client,
            String username,
            boolean validate
    ) {
        validateUserExists(
                client,
                username
        );

        requestSender.sendDeleteUserRequest(
                client,
                username
        );
    }

    protected void validateUserExists(
            T client,
            String username
    ) {
        if (!modelExistenceValidator.validateUserExistence(
                client,
                username
        )) {
            throw new UserDoesNotExistOnTheClientException(
                    "User with username "
                            + username
                            + " doesn't exist on the client with id "
                            + client.getId()
            );
        }
    }

    protected void validateUserDoesNotExists(
            T client,
            String username
    ) {
        if (modelExistenceValidator.validateUserExistence(
                client,
                username
        )) {
            throw new UserAlreadyExistsOnTheClientException(
                    "User with username "
                            + username
                            + " already exists on the client with id "
                            + client.getId()
            );
        }
    }
}
