package com.iliauni.idpsyncservice.idp;

import com.iliauni.idpsyncservice.model.Client;
import com.iliauni.idpsyncservice.model.Usergroup;
import java.util.List;
import java.util.Map;

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
            Map<String, List<Usergroup>> differenceMap
    );
}
