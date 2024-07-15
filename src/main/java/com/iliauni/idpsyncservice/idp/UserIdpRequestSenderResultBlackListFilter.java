package com.iliauni.idpsyncservice.idp;

import com.iliauni.idpsyncservice.model.Client;
import com.iliauni.idpsyncservice.model.User;

import java.util.List;

/**
 * An interface for blacklisting user related request results from a request sender.
 *
 * @param <T> a type of client from which the blacklist will be obtained.
 */
public interface UserIdpRequestSenderResultBlackListFilter<T extends Client> extends IdpRequestSenderResultBlackListFilter<T, User> {
    @Override
    User filter(
            T client,
            User userToFilter
    );

    @Override
    List<User> filter(
            T client,
            List<User> usersToFilter
    );
}
