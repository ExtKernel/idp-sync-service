package com.iliauni.idpsyncservice.idp;

import com.fasterxml.jackson.databind.JsonNode;
import com.iliauni.idpsyncservice.exception.UserAlreadyExistsOnTheClientException;
import com.iliauni.idpsyncservice.exception.UserDoesNotExistOnTheClientException;
import com.iliauni.idpsyncservice.model.Client;
import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.service.ClientService;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class GenericIdpUserManager<T extends Client> implements IdpUserManager<T> {

    @Getter(AccessLevel.PROTECTED)
    private final ClientService<T> clientService;

    @Getter(AccessLevel.PROTECTED)
    private final IdpJsonObjectMapper jsonObjectMapper;

    @Getter(AccessLevel.PROTECTED)
    private final IdpUserRequestSender<T> requestSender;

    @Getter(AccessLevel.PROTECTED)
    private final IdpModelExistenceValidator<T> modelExistenceValidator;

    @Getter(AccessLevel.PROTECTED)
    private final UserIdpRequestSenderResultBlackListFilter<T> blackListFilter;

    public GenericIdpUserManager(
            ClientService<T> clientService,
            IdpJsonObjectMapper jsonObjectMapper,
            IdpUserRequestSender<T> requestSender,
            IdpModelExistenceValidator<T> modelExistenceValidator,
            UserIdpRequestSenderResultBlackListFilter<T> blackListFilter
    ) {
        this.clientService = clientService;
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
    public synchronized User createUser(
            T client,
            User user,
            boolean validate
    ) {
        validateUserDoesNotExists(
                client,
                user.getUsername()
        );

        User createdUser = requestSender.sendCreateUserRequest(
                client,
                user
        );

        // clear the cache after modifying users on the client
        // if not cleared,
        // ModelExistenceValidator and other classes
        // that depend on the output of getUsers are likely to break
        clientService.clearClientUserCache(client);

        return createdUser;
    }

    /**
     * @param validate validate that the user exists
     *                before sending a request to get it or not.
     */
    @Override
    public synchronized User getUser(
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
    public synchronized List<User> getUsers(T client) {
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
     * @param validate validate that the user exists
     *                before sending a request to update its password or not.
     */
    @Override
    public synchronized String updateUserPassword(
            T client,
            String username,
            String newPassword,
            boolean validate
    ) {
        validateUserExists(
                client,
                username
        );

        String updatedPassword = requestSender.sendUpdateUserPasswordRequest(
                client,
                username,
                newPassword
        );

        // clear the cache after modifying users on the client
        // if not cleared,
        // ModelExistenceValidator and other classes
        // that depend on the output of getUsers are likely to break
        clientService.clearClientUserCache(client);

        return updatedPassword;
    }

    /**
     * @param validate validate that the user exists
     *                before sending a request to delete it or not.
     */
    @Override
    public synchronized void deleteUser(
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

        // clear the cache after modifying users on the client
        // if not cleared,
        // ModelExistenceValidator and other classes
        // that depend on the output of getUsers are likely to break
        clientService.clearClientUserCache(client);
    }

    @Override
    public void validateUserExists(
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

    @Override
    public void validateUserDoesNotExists(
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
