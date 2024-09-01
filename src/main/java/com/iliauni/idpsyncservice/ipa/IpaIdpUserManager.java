package com.iliauni.idpsyncservice.ipa;

import com.fasterxml.jackson.databind.JsonNode;
import com.iliauni.idpsyncservice.idp.*;
import com.iliauni.idpsyncservice.model.IpaClient;
import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IpaIdpUserManager extends GenericIdpUserManager<IpaClient> {

    @Autowired
    public IpaIdpUserManager(
            ClientService<IpaClient> clientService,
            @Qualifier("ipaIdpJsonObjectMapper") IdpJsonObjectMapper jsonObjectMapper,
            IdpUserRequestSender<IpaClient> requestSender,
            @Lazy IdpModelExistenceValidator<IpaClient> modelExistenceValidator,
            UserIdpRequestSenderResultBlackListFilter<IpaClient> blackListFilter
    ) {
        super(
                clientService,
                jsonObjectMapper,
                requestSender,
                modelExistenceValidator,
                blackListFilter
        );
    }

    @Override
    public User getUser(
            IpaClient client,
            String username,
            boolean validate
    ) {
        validateUserExists(
                client,
                username
        );

        return super.getBlackListFilter().filter(
                client,
                super.getJsonObjectMapper().mapUserJsonNodeToUser(
                        getDirectResult(
                                super.getRequestSender().sendGetUserRequest(
                                        client,
                                        username
                                )
                        )
                )
        );
    }

    @Override
    public List<User> getUsers(IpaClient client) {
        List<User> users = new ArrayList<>();

        // iterate over JSON nodes which represent users and map each one to an object
        for (JsonNode user : getDirectResult(super.getRequestSender().sendGetUsersRequest(client))) {
            users.add(super.getJsonObjectMapper().mapUserJsonNodeToUser(user));
        }

        return super.getBlackListFilter().filter(
                client,
                users
        );
    }

    /**
     * Returns a requested from FreeIPA API result as the API doesn't return
     * it directly and wraps into a JSON path "result.result".
     *
     * @param ipaOutputJson an output of a request to the FreeIPA API.
     * @return the requested data without unnecessary API details.
     */
    private JsonNode getDirectResult(JsonNode ipaOutputJson) {
        return ipaOutputJson
                .path("result")
                .path("result");
    }
}
