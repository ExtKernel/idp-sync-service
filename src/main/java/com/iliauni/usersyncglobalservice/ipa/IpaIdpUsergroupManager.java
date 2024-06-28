package com.iliauni.usersyncglobalservice.ipa;

import com.fasterxml.jackson.databind.JsonNode;
import com.iliauni.usersyncglobalservice.idp.*;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IpaIdpUsergroupManager extends GenericIdpUsergroupManager<IpaClient> {

    @Autowired
    public IpaIdpUsergroupManager(
            @Qualifier("ipaIdpJsonObjectMapper") IdpJsonObjectMapper jsonObjectMapper,
            IdpUsergroupRequestSender<IpaClient> requestSender,
            IdpModelExistenceValidator<IpaClient> modelExistenceValidator,
            UsergroupIdpRequestSenderResultBlackListFilter<IpaClient> blackListFilter
    ) {
        super(
                jsonObjectMapper,
                requestSender,
                modelExistenceValidator,
                blackListFilter
        );
    }

    @Override
    public Usergroup getUsergroup(IpaClient client, String usergroupName) {
        validateUsergroupExists(
                client,
                usergroupName
        );

        return super.getBlackListFilter().filter(
                client,
                super.getJsonObjectMapper()
                        .mapUsergroupJsonNodeToUsergroup(
                                getDirectResult(super.getRequestSender()
                                        .sendGetUsergroupRequest(
                                                client,
                                                usergroupName
                                        )))
        );
    }

    @Override
    public List<Usergroup> getUsergroups(IpaClient client) {
        List<Usergroup> usergroups = new ArrayList<>();

        for (JsonNode usergroupJson : getDirectResult(
                super.getRequestSender().sendGetUsergroupsRequest(client))) {
            Usergroup usergroup = super.getJsonObjectMapper().mapUsergroupJsonNodeToUsergroup(usergroupJson);
            usergroup.setUsers(getUsergroupMembers(
                    client,
                    usergroup.getName())
            );
            usergroups.add(usergroup);
        }

        return super.getBlackListFilter().filter(
                client,
                usergroups
        );
    }

    @Override
    public List<User> getUsergroupMembers(IpaClient client, String usergroupName) {
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
        for (JsonNode userJson : getDirectResult(super.getRequestSender().sendGetUsergroupMembersRequest(
                client,
                usergroupName
        ))) {
            users.add(super.getJsonObjectMapper().mapUserJsonNodeToUser(userJson));
        }

        return users;
    }

    /**
     * Returns the requested from FreeIPA API result as the API doesn't return
     * it directly and wraps into a JSON path "result.result"
     *
     * @param ipaOutputJson an output of a request to the FreeIPA API
     * @return the requested data without unnecessary API details
     */
    private JsonNode getDirectResult(JsonNode ipaOutputJson) {
        return ipaOutputJson
                .path("result")
                .path("result");
    }
}
