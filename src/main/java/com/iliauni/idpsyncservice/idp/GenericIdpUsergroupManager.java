package com.iliauni.idpsyncservice.idp;

import com.fasterxml.jackson.databind.JsonNode;
import com.iliauni.idpsyncservice.exception.UserDoesNotExistOnTheClientException;
import com.iliauni.idpsyncservice.exception.UsergroupAlreadyExistsOnTheClientException;
import com.iliauni.idpsyncservice.exception.UsergroupDoesNotExistOnTheClientException;
import com.iliauni.idpsyncservice.exception.UsergroupMemberAlreadyExistsOnTheClientException;
import com.iliauni.idpsyncservice.model.Client;
import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.model.Usergroup;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericIdpUsergroupManager<T extends Client> implements IdpUsergroupManager<T> {

    @Getter(AccessLevel.PROTECTED)
    private final IdpJsonObjectMapper jsonObjectMapper;

    @Getter(AccessLevel.PROTECTED)
    private final IdpUsergroupRequestSender<T> requestSender;

    @Getter(AccessLevel.PROTECTED)
    private final IdpModelExistenceValidator<T> modelExistenceValidator;

    @Getter(AccessLevel.PROTECTED)
    private final UsergroupIdpRequestSenderResultBlackListFilter<T> blackListFilter;

    public GenericIdpUsergroupManager(
            IdpJsonObjectMapper jsonObjectMapper,
            IdpUsergroupRequestSender<T> requestSender,
            IdpModelExistenceValidator<T> modelExistenceValidator,
            UsergroupIdpRequestSenderResultBlackListFilter<T> blackListFilter
    ) {
        this.jsonObjectMapper = jsonObjectMapper;
        this.requestSender = requestSender;
        this.modelExistenceValidator = modelExistenceValidator;
        this.blackListFilter = blackListFilter;
    }

    /**
     * @param validate validate that the user group doesn't exist
     *                before sending a request to create or not.
     */
    @Override
    public Usergroup createUsergroup(
            T client,
            Usergroup usergroup,
            boolean validate
    ) {
        if (validate) validateUsergroupDoesNotExist(
                client,
                usergroup.getName()
        );

        return requestSender.sendCreateUsergroupRequest(
                client,
                usergroup
        );
    }

    /**
     * @param validate validate that the user group exists
     *                and hasn't the user as a member already
     *                before sending a request to add the member or not.
     */
    @Override
    public void addUsergroupMember(
            T client,
            String usergroupName,
            String username,
            boolean validate
    ) {
        if (validate) {
            validateUsergroupExists(
                    client,
                    usergroupName
            );
            validateUsergroupMemberDoesNotExist(
                    client,
                    usergroupName,
                    username
            );
        }

        requestSender.sendAddUsergroupMemberRequest(
                client,
                usergroupName,
                username
        );
    }

    /**
     * @param validate validate that the user group exists
     *                before sending a request to get it or not.
     */
    @Override
    public Usergroup getUsergroup(
            T client,
            String usergroupName,
            boolean validate
    ) {
        if (validate) validateUsergroupExists(
                client,
                usergroupName
        );

        return blackListFilter.filter(
                client,
                jsonObjectMapper.mapUsergroupJsonNodeToUsergroup(requestSender.sendGetUsergroupRequest(
                        client,
                        usergroupName
                ))
        );
    }

    @Override
    public List<Usergroup> getUsergroups(T client) {
        List<Usergroup> usergroups = new ArrayList<>();

        for (JsonNode usergroupJson : requestSender.sendGetUsergroupsRequest(client)) {
            Usergroup usergroup = jsonObjectMapper.mapUsergroupJsonNodeToUsergroup(usergroupJson);

            // call getUsergroupMembers without validation
            // because if called from this method,
            // it means the usergroup exists and doesn't need a validation

            if (blackListFilter.filter(
                    client,
                    usergroup
            ) != null) usergroup.setUsers(getUsergroupMembers(
                    client,
                    usergroup.getName(),
                    false
                )
            );
            usergroups.add(usergroup);
        }

        return blackListFilter.filter(
                client,
                usergroups
        );
    }

    /**
     * @param validate validate that the user group exists before
     *                sending a request to get its members or not.
     */
    @Override
    public List<User> getUsergroupMembers(
            T client,
            String usergroupName,
            boolean validate
    ) {
        if (validate) validateUsergroupExists(
                client,
                usergroupName
        );

        List<User> users = new ArrayList<>();

        // iterate over JSON nodes which represent members
        // of the given user group and map each one to an object
        // unlike user groups, a user retrieved from a client
        // at best consists of a username, first name, last name and email
        // so no extra mapping logic is required
        for (JsonNode userJson : requestSender.sendGetUsergroupMembersRequest(
                client,
                usergroupName
        )) {
            users.add(jsonObjectMapper.mapUserJsonNodeToUser(userJson));
        }

        return users;
    }

    /**
     * @param validate validate that the user group exists
     *                before sending a request to delete it or not.
     */
    @Override
    public void deleteUsergroup(
            T client,
            String usergroupName,
            boolean validate
    ) {
        if (validate) validateUsergroupExists(
                client,
                usergroupName
        );

        requestSender.sendDeleteUsergroupRequest(
                client,
                usergroupName
        );
    }

    /**
     * @param validate validate that the user exists
     *                and is a member of the usergroup
     *                before sending a request to remove the member or not.
     */
    @Override
    public void removeUsergroupMember(
            T client,
            String usergroupName,
            String username,
            boolean validate
    ) {
        if (validate) {
            validateUserExists(
                    client,
                    username
            );
            validateUsergroupMemberExists(
                    client,
                    usergroupName,
                    username
            );
        }

        requestSender.sendRemoveUsergroupMemberRequest(
                client,
                usergroupName,
                username
        );
    }

    protected void validateUsergroupExists(
            T client,
            String usergroupName
    ) {
        if (!modelExistenceValidator.validateUsergroupExistence(
                client,
                usergroupName
        )) {
            throw new UsergroupDoesNotExistOnTheClientException(
                    "User group with name(id) "
                            + usergroupName
                            + " doesn't exist on the client with id "
                            + client.getId()
            );
        }
    }

    protected void validateUsergroupDoesNotExist(
            T client,
            String usergroupName
    ) {
        if (modelExistenceValidator.validateUsergroupExistence(
                client,
                usergroupName
        )) {
            throw new UsergroupAlreadyExistsOnTheClientException(
                    "User group with name(id) "
                            + usergroupName
                            + " already exists on the client with id "
                            + client.getId()
            );
        }
    }

    protected void validateUsergroupMemberExists(
            T client,
            String usergroupName,
            String username
    ) {
        if (!modelExistenceValidator.validateUsergroupMemberExistence(
                client,
                usergroupName,
                username
        )) {
            throw new UsergroupMemberAlreadyExistsOnTheClientException(
                    "User with username "
                            + username
                            + " is not a member of user group with name(id) "
                            + usergroupName
                            + " on the client with id "
                            + client.getId()
            );
        }
    }

    protected void validateUsergroupMemberDoesNotExist(
            T client,
            String usergroupName,
            String username
    ) {
        if (modelExistenceValidator.validateUsergroupMemberExistence(
                client,
                usergroupName,
                username
        )) {
            throw new UsergroupMemberAlreadyExistsOnTheClientException(
                    "User with username "
                            + username
                            + " is already a member of user group with name(id) "
                            + usergroupName
                            + " on the client with id "
                            + client.getId()
            );
        }
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
}
