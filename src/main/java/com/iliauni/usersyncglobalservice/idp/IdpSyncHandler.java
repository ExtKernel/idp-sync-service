package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.model.Client;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Handles the synchronization process of IDP models.
 */
public interface IdpSyncHandler<T extends Client> {
    /**
     * Handles synchronization of user groups.
     *
     * @param differenceMap a map, which contains changes
     *                     to be applied while performing synchronization.
     */
    void syncUsergroupChanges(
            T client,
            Map<String, List<Optional<Usergroup>>> differenceMap
    );

    void forceUsergroupChanges(
            T client,
            List<Optional<Usergroup>> usergroups,
            boolean isNew,
            boolean isAltered
    );

    void forceUserChanges(
            T client,
            List<Optional<User>> users,
            boolean isNew
    );
}
