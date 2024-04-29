package com.iliauni.usersyncglobalservice.idp;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.reflect.TypeToken;
import com.iliauni.usersyncglobalservice.exception.UsergroupAlreadyExistsOnTheClientException;
import com.iliauni.usersyncglobalservice.exception.UsergroupDoesNotExistOnTheClientException;
import com.iliauni.usersyncglobalservice.exception.UsergroupUserAlreadyExistsOnTheClientException;
import com.iliauni.usersyncglobalservice.model.Client;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Lazy
@Component
public class GenericIdpUsergroupManager<T extends Client> implements IdpUsergroupManager<T> {
    private final IdpJsonObjectMapper jsonObjectMapper;
    private final IdpUsergroupRequestSender<T> requestSender;
    private final IdpModelExistenceValidator<T> modelExistenceValidator;

    @Autowired
    public GenericIdpUsergroupManager(
            @Lazy GenericIdpModelManagerBeanDeterminer beanDeterminer,
            @Lazy IdpModelExistenceValidator<T> modelExistenceValidator
    ) {
        Type clientType = new TypeToken<T>(getClass()){}.getType();

        this.jsonObjectMapper = beanDeterminer.determineJsonObjectMapper(clientType);
        this.requestSender = beanDeterminer.determineIdpUsergroupRequestSender(clientType);
        this.modelExistenceValidator = modelExistenceValidator;
    }

    @Override
    public Usergroup createUsergroup(
            T client,
            Usergroup usergroup
    ) throws UsergroupAlreadyExistsOnTheClientException {
        if (!modelExistenceValidator.validateUsergroupExistence(client, usergroup.getName())) {
            return requestSender.sendCreateUsergroupRequest(
                    client,
                    usergroup
            );
        } else {
            throw new UsergroupAlreadyExistsOnTheClientException(
                    "Can't create a user group on the client. User group with name(id) "
                            + usergroup.getName()
                            + " already exists on the client with id "
                            + client.getId()
            );
        }
    }

    /**
     * @inheritDoc
     * At the moment, this method serves just as a proxy to the request sender
     */
    @Override
    public void addUsergroupMember(
            T client,
            String usergroupName,
            String username
    ) throws UsergroupUserAlreadyExistsOnTheClientException {
        if (!modelExistenceValidator.validateUsergroupUserExistence(
                client,
                usergroupName,
                username
        )) {
            requestSender.sendAddUsergroupMemberRequest(
                    client,
                    usergroupName,
                    username
            );
        } else {
            throw new UsergroupUserAlreadyExistsOnTheClientException(
                    "Can't a member to a user group with name(id): " + usergroupName
                            + ". A user with username " + username
                            + " already is a member of the " + usergroupName + " user group"
            );
        }
    }

    @Override
    public Usergroup getUsergroup(
            T client,
            String usergroupName
    ) {
        // map user group JSON node to an object
        Usergroup usergroup = jsonObjectMapper.mapUsergroupJsonNodeToUsergroup(
                requestSender.sendGetUsergroupMembersRequest(
                        client,
                        usergroupName
                )
        );
        // set users as the specification of the method requires
        // to return a full representation of a user group if possible
        usergroup.setUsers(getUsergroupMembers(
                client,
                usergroupName
        ));

        return usergroup;
    }

    @Override
    public List<Usergroup> getUsergroups(T client) {
        List<Usergroup> usergroups = new ArrayList<>();

        // iterate over JSON nodes which represent user groups and map each one to an object
        for (JsonNode usergroupJson : requestSender.sendGetUsergroupsRequest(client)) {
            Usergroup usergroup = jsonObjectMapper.mapUsergroupJsonNodeToUsergroup(usergroupJson);

            // set users as the specification of the method requires
            // to return a full representation of a user group if possible
            usergroup.setUsers(getUsergroupMembers(
                    client,
                    usergroup.getName()
            ));

            usergroups.add(jsonObjectMapper.mapUsergroupJsonNodeToUsergroup(usergroupJson));
        }

        return usergroups;
    }

    @Override
    public List<User> getUsergroupMembers(
            T client,
            String usergroupName
    ) {
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
     * @inheritDoc
     * At the moment, this method serves just as a proxy to the request sender
     */
    @Override
    public void deleteUsergroup(
            T client,
            String usergroupName
    ) throws UsergroupDoesNotExistOnTheClientException {
        if (modelExistenceValidator.validateUsergroupExistence(client, usergroupName)) {
            requestSender.sendDeleteUsergroupRequest(
                    client,
                    usergroupName
            );
        } else {
            throw new UsergroupDoesNotExistOnTheClientException(
                    "Can't delete a user group from the client. User group with name(id) "
                            + usergroupName
                            + " doesn't exists on the client with id "
                            + client.getId()
            );
        }
    }

    /**
     * @inheritDoc
     * At the moment, this method serves just as a proxy to the request sender
     */
    @Override
    public void removeUsergroupMember(
            T client,
            String usergroupName,
            String username
    ) {
        requestSender.sendRemoveUsergroupMemberRequest(
                client,
                usergroupName,
                username
        );
    }
}
