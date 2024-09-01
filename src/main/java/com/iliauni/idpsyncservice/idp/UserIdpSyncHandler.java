package com.iliauni.idpsyncservice.idp;

import com.iliauni.idpsyncservice.model.Client;
import com.iliauni.idpsyncservice.model.User;

import java.util.List;
import java.util.Map;

/**
 * An interface to handle synchronization of users in an Identity Provider (IDP) context.
 *
 * @param <T> a type of client to perform synchronization on. Defines specifics of synchronization.
 */
public interface UserIdpSyncHandler<T extends Client> {
    /**
     * Handles synchronization of users.
     *
     * @param differenceMap a map, which contains changes
     *                     to be applied while performing synchronization.
     */
    void sync(
            T client,
            Map<String, List<User>> differenceMap
    );
}