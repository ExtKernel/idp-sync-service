package com.iliauni.idpsyncservice.ipa;

import com.fasterxml.jackson.databind.JsonNode;
import com.iliauni.idpsyncservice.idp.GenericIdpUsergroupManager;
import com.iliauni.idpsyncservice.idp.IdpJsonObjectMapper;
import com.iliauni.idpsyncservice.idp.IdpModelExistenceValidator;
import com.iliauni.idpsyncservice.idp.IdpUserManager;
import com.iliauni.idpsyncservice.idp.IdpUsergroupRequestSender;
import com.iliauni.idpsyncservice.idp.UsergroupIdpRequestSenderResultBlackListFilter;
import com.iliauni.idpsyncservice.model.IpaClient;
import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.model.Usergroup;
import com.iliauni.idpsyncservice.service.ClientService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class IpaIdpUsergroupManager extends GenericIdpUsergroupManager<IpaClient> {

    @Autowired
    public IpaIdpUsergroupManager(
            ClientService<IpaClient> clientService,
            @Qualifier("ipaIdpJsonObjectMapper") IdpJsonObjectMapper jsonObjectMapper,
            IdpUsergroupRequestSender<IpaClient> requestSender,
            @Lazy IdpModelExistenceValidator<IpaClient> modelExistenceValidator,
            @Lazy IdpUserManager<IpaClient> userManager,
            UsergroupIdpRequestSenderResultBlackListFilter<IpaClient> blackListFilter
    ) {
        super(
                clientService,
                jsonObjectMapper,
                requestSender,
                modelExistenceValidator,
                userManager,
                blackListFilter
        );
    }

    @Override
    public synchronized Usergroup getUsergroup(
            IpaClient client,
            String usergroupName,
            boolean validate
    ) {
        if (validate) validateUsergroupExists(
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
    public synchronized List<Usergroup> getUsergroups(IpaClient client) {
        List<Usergroup> usergroups = new ArrayList<>();

        for (JsonNode usergroupJson : getDirectResult(
                super.getRequestSender().sendGetUsergroupsRequest(client))) {
            Usergroup usergroup = super.getJsonObjectMapper().mapUsergroupJsonNodeToUsergroup(usergroupJson);

            // call getUsergroupMembers without validation
            // because if called from this method,
            // it means the usergroup exists and doesn't need a validation
            usergroup.setUsers(getUsergroupMembers(
                    client,
                    usergroup.getName(),
                    false
                )
            );
            usergroups.add(usergroup);
        }

        return super.getBlackListFilter().filter(
                client,
                usergroups
        );
    }

    @Override
    public synchronized List<User> getUsergroupMembers(
            IpaClient client,
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
        for (JsonNode userJson : getDirectResult(super.getRequestSender().sendGetUsergroupMembersRequest(
                client,
                usergroupName
        )).path("member_user")) {
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
