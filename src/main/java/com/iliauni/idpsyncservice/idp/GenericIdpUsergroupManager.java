package com.iliauni.idpsyncservice.idp;

import com.fasterxml.jackson.databind.JsonNode;
import com.iliauni.idpsyncservice.exception.UserDoesNotExistOnTheClientException;
import com.iliauni.idpsyncservice.exception.UsergroupAlreadyExistsOnTheClientException;
import com.iliauni.idpsyncservice.exception.UsergroupDoesNotExistOnTheClientException;
import com.iliauni.idpsyncservice.exception.UsergroupMemberAlreadyExistsOnTheClientException;
import com.iliauni.idpsyncservice.exception.UsergroupMemberDoesNotExistOnTheClientException;
import com.iliauni.idpsyncservice.model.Client;
import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.model.Usergroup;
import com.iliauni.idpsyncservice.service.ClientService;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class GenericIdpUsergroupManager<T extends Client> implements IdpUsergroupManager<T> {

    @Getter(AccessLevel.PROTECTED)
    private final ClientService<T> clientService;

    @Getter(AccessLevel.PROTECTED)
    private final IdpJsonObjectMapper jsonObjectMapper;

    @Getter(AccessLevel.PROTECTED)
    private final IdpUsergroupRequestSender<T> requestSender;

    @Getter(AccessLevel.PROTECTED)
    private final IdpModelExistenceValidator<T> modelExistenceValidator;

    @Getter(AccessLevel.PROTECTED)
    private final IdpUserManager<T> userManager;

    @Getter(AccessLevel.PROTECTED)
    private final UsergroupIdpRequestSenderResultBlackListFilter<T> blackListFilter;

    public GenericIdpUsergroupManager(
            ClientService<T> clientService,
            IdpJsonObjectMapper jsonObjectMapper,
            IdpUsergroupRequestSender<T> requestSender,
            IdpModelExistenceValidator<T> modelExistenceValidator,
            IdpUserManager<T> userManager,
            UsergroupIdpRequestSenderResultBlackListFilter<T> blackListFilter
    ) {
        this.clientService = clientService;
        this.jsonObjectMapper = jsonObjectMapper;
        this.requestSender = requestSender;
        this.modelExistenceValidator = modelExistenceValidator;
        this.userManager = userManager;
        this.blackListFilter = blackListFilter;
    }

    /**
     * @param validate validate that the user group doesn't exist
     *                before sending a request to create or not.
     */
    @Override
    public synchronized Usergroup createUsergroup(
            T client,
            Usergroup usergroup,
            boolean validate
    ) {
        if (validate) validateUsergroupDoesNotExist(
                client,
                usergroup.getName()
        );

        Usergroup createdUsergroup = requestSender.sendCreateUsergroupRequest(
                client,
                usergroup
        );

        // clear the cache after modifying user groups on the client
        // if not cleared,
        // ModelExistenceValidator and other classes
        // that depend on the output of getUsergroups are likely to break
        clientService.clearClientUsergroupsCache(client);

        return createdUsergroup;
    }

    /**
     * @param validate validate that the user group exists
     *                and hasn't the user as a member already
     *                before sending a request to add the member or not.
     */
    @Override
    public synchronized void addUsergroupMember(
            T client,
            String usergroupName,
            String username,
            boolean validate
    ) {
        if (validate) {
            try {
                userManager.validateUserExists(
                        client,
                        username
                );
            } catch (UserDoesNotExistOnTheClientException exception) {
                log.info(
                        "User group with username "
                                + username
                                + " in going to be created on the client with id "
                                + client.getId()
                                + " during process of adding a user group member to a user group with name "
                                + usergroupName
                                + " ,because the user doesn't exist on the client"
                );

                // don't validate, because if the request
                // comes from here, we know that the user doesn't exist
                // and it's ok
                userManager.createUser(
                        client,
                        new User(username),
                        false
                );
            }
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

        // clear the cache after modifying user groups on the client
        // if not cleared,
        // ModelExistenceValidator and other classes
        // that depend on the output of getUsergroups are likely to break
        clientService.clearClientUsergroupsCache(client);
    }

    /**
     * @param validate validate that the user group exists
     *                before sending a request to get it or not.
     */
    @Override
    public synchronized Usergroup getUsergroup(
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
    public synchronized List<Usergroup> getUsergroups(T client) {
        List<Usergroup> usergroups = new ArrayList<>();

        for (JsonNode usergroupJson : requestSender.sendGetUsergroupsRequest(client)) {
            Usergroup usergroup = jsonObjectMapper.mapUsergroupJsonNodeToUsergroup(usergroupJson);

            // if the user group is blacklisted
            // there's no point in wasting time on retrieving its members
            if (blackListFilter.filter(
                    client,
                    usergroup
            ) != null){
                // call getUsergroupMembers without validation
                // because if called from this method,
                // it means the usergroup exists and doesn't need a validation
                usergroup.setUsers(getUsergroupMembers(
                                client,
                                usergroup.getName(),
                                false
                        )
                );
            }

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
    public synchronized List<User> getUsergroupMembers(
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
    public synchronized void deleteUsergroup(
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

        // clear the cache after modifying user groups on the client
        // if not cleared,
        // ModelExistenceValidator and other classes
        // that depend on the output of getUsergroups are likely to break
        clientService.clearClientUsergroupsCache(client);
    }

    /**
     * @param validate validate that the user exists
     *                and is a member of the usergroup
     *                before sending a request to remove the member or not.
     */
    @Override
    public synchronized void removeUsergroupMember(
            T client,
            String usergroupName,
            String username,
            boolean validate
    ) {
        if (validate) {
            try {
                userManager.validateUserExists(
                        client,
                        username
                );
            } catch (UserDoesNotExistOnTheClientException exception) {
                log.info(
                        "A user group with username "
                                + username
                                + " wasn't removed as a member from usergroup with name "
                                + usergroupName
                                + " , because the user doesn't exist on the client with id "
                                + client.getId()
                );
            }
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

        // clear the cache after modifying user groups on the client
        // if not cleared,
        // ModelExistenceValidator and other classes
        // that depend on the output of getUsergroups are likely to break
        clientService.clearClientUsergroupsCache(client);
    }

    public void validateUsergroupExists(
            T client,
            String usergroupName
    ) {
        if (!modelExistenceValidator.validateUsergroupExistence(
                client,
                usergroupName
        )) {
            throw new UsergroupDoesNotExistOnTheClientException(
                    "A user group with name(id) "
                            + usergroupName
                            + " doesn't exist on the client with id "
                            + client.getId()
            );
        }
    }

    public void validateUsergroupDoesNotExist(
            T client,
            String usergroupName
    ) {
        if (modelExistenceValidator.validateUsergroupExistence(
                client,
                usergroupName
        )) {
            throw new UsergroupAlreadyExistsOnTheClientException(
                    "A user group with name(id) "
                            + usergroupName
                            + " already exists on the client with id "
                            + client.getId()
            );
        }
    }

    public void validateUsergroupMemberExists(
            T client,
            String usergroupName,
            String username
    ) {
        if (!modelExistenceValidator.validateUsergroupMemberExistence(
                client,
                usergroupName,
                username
        )) {
            throw new UsergroupMemberDoesNotExistOnTheClientException(
                    "A user group with name(id) "
                            + username
                            + " is not a member of user group with name(id) "
                            + usergroupName
                            + " on the client with id "
                            + client.getId()
            );
        }
    }

    public void validateUsergroupMemberDoesNotExist(
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
                    "A user group with name(id) "
                            + username
                            + " is already a member of user group with name(id) "
                            + usergroupName
                            + " on the client with id "
                            + client.getId()
            );
        }
    }
}
