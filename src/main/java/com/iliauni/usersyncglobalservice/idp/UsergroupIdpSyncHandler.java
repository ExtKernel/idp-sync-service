package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.model.Client;
import com.iliauni.usersyncglobalservice.model.Usergroup;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * An interface to handle synchronization of user groups in an Identity Provider (IDP) context.
 *
 * @param <T> a type of client to perform synchronization on. Defines specifics of synchronization.
 */
public interface UsergroupIdpSyncHandler<T extends Client> {
    /**
     * Handles synchronization of user groups.
     *
     * @param differenceMap a map, which contains changes
     *                     to be applied while performing synchronization.
     */
    void sync(
            T client,
            Map<String, List<Optional<Usergroup>>> differenceMap
    );
}
