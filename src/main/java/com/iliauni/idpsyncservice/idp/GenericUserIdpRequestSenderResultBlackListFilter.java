package com.iliauni.idpsyncservice.idp;

import com.iliauni.idpsyncservice.model.Client;
import com.iliauni.idpsyncservice.model.User;

import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericUserIdpRequestSenderResultBlackListFilter<T extends Client> implements UserIdpRequestSenderResultBlackListFilter<T> {

    public User filter(
            T client,
            User userToFilter
    ) {
        return client.getUserBlacklist().stream()
                .anyMatch(username -> username.equals(userToFilter.getUsername()))
                ? null : userToFilter;
    }


    public List<User> filter(
            T client,
            List<User> usersToFilter
    ) {
        return usersToFilter.stream()
                .filter(user ->
                        client.getUserBlacklist().stream()
                                .noneMatch(username -> username.equals(user.getUsername())))
                .collect(Collectors.toList());
    }
}
