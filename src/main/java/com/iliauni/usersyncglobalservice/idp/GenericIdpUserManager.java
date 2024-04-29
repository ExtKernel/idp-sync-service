package com.iliauni.usersyncglobalservice.idp;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.reflect.TypeToken;
import com.iliauni.usersyncglobalservice.model.Client;
import com.iliauni.usersyncglobalservice.model.User;
import java.lang.reflect.Type;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Lazy
@Component
public class GenericIdpUserManager<T extends Client> implements IdpUserManager<T> {
    private final IdpJsonObjectMapper jsonObjectMapper;
    private final IdpUserRequestSender<T> requestSender;

    public GenericIdpUserManager(GenericIdpModelManagerBeanDeterminer beanDeterminer) {
        Type clientType = new TypeToken<T>(getClass()){}.getType();

        this.jsonObjectMapper = beanDeterminer.determineJsonObjectMapper(clientType);
        this.requestSender = beanDeterminer.determineIdpUserRequestSender(clientType);
    }

    /**
     * @inheritDoc
     * At the moment, this method serves just as a proxy to the request sender
     */
    @Override
    public User createUser(
            T client,
            User user
    ) {
        return requestSender.sendCreateUserRequest(
                client,
                user
        );
    }

    @Override
    public User getUser(
            T client,
            String username
    ) {
        return jsonObjectMapper.mapUserJsonNodeToUser(
                getDirectResult(
                        requestSender.sendGetUserRequest(
                                client,
                                username
                        )
                )
        );
    }

    @Override
    public List<User> getUsers(T client) {
        List<User> users = new ArrayList<>();

        // iterate over JSON nodes which represent users and map each one to an object
        for (JsonNode user : getDirectResult(requestSender.sendGetUsersRequest(client))) {
            users.add(jsonObjectMapper.mapUserJsonNodeToUser(user));
        }

        return users;
    }

    /**
     * @inheritDoc
     * At the moment, this method serves just as a proxy to the request sender
     */
    @Override
    public String updateUserPassword(
            T client,
            String username,
            String newPassword
    ) {
        return requestSender.sendUpdateUserPasswordRequest(
                client,
                username,
                newPassword
        );
    }

    /**
     * @inheritDoc
     * At the moment, this method serves just as a proxy to the request sender
     */
    @Override
    public void deleteUser(
            T client,
            String username
    ) {
        requestSender.sendDeleteUserRequest(
                client,
                username
        );
    }

    /**
     * Returns the requested from FreeIPA API result as the API doesn't return
     * it directly and wraps into a JSON path "result.result"
     *
     * @param ipaOutputJson an output of a request to the FreeIPA API
     * @return the requested data without unnecessary API details
     */
    private JsonNode getDirectResult(JsonNode ipaOutputJson) {
        return ipaOutputJson.path("result").path("result");
    }
}
