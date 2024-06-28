package com.iliauni.usersyncglobalservice.idp;

import com.fasterxml.jackson.databind.JsonNode;
import com.iliauni.usersyncglobalservice.exception.UserDoesNotExistOnTheClientException;
import com.iliauni.usersyncglobalservice.exception.UsergroupAlreadyExistsOnTheClientException;
import com.iliauni.usersyncglobalservice.exception.UsergroupDoesNotExistOnTheClientException;
import com.iliauni.usersyncglobalservice.exception.UsergroupMemberAlreadyExistsOnTheClientException;
import com.iliauni.usersyncglobalservice.model.Client;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
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

    @Override
    public Usergroup createUsergroup(
            T client,
            Usergroup usergroup
    ) {
        validateUsergroupDoesNotExist(
                client,
                usergroup.getName()
        );

        return requestSender.sendCreateUsergroupRequest(
                client,
                usergroup
        );
    }

    @Override
    public void addUsergroupMember(
            T client,
            String usergroupName,
            String username
    ) {
        validateUsergroupExists(
                client,
                usergroupName
        );
        validateUsergroupMemberDoesNotExist(
                client,
                usergroupName,
                username
        );

        requestSender.sendAddUsergroupMemberRequest(
                client,
                usergroupName,
                username
        );
    }

    @Override
    public Usergroup getUsergroup(
            T client,
            String usergroupName
    ) {
        validateUsergroupExists(
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
            usergroup.setUsers(getUsergroupMembers(
                    client,
                    usergroup.getName())
            );
            usergroups.add(usergroup);
        }

        return blackListFilter.filter(
                client,
                usergroups
        );
    }

    @Override
    public List<User> getUsergroupMembers(
            T client,
            String usergroupName
    ) {
        validateUsergroupExists(
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

    @Override
    public void deleteUsergroup(
            T client,
            String usergroupName
    ) {
        validateUsergroupExists(
                client,
                usergroupName
        );

        requestSender.sendDeleteUsergroupRequest(
                client,
                usergroupName
        );
    }

    @Override
    public void removeUsergroupMember(
            T client,
            String usergroupName,
            String username
    ) {
        validateUserExists(
                client,
                username
        );
        validateUsergroupMemberExists(
                client,
                usergroupName,
                username
        );

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
