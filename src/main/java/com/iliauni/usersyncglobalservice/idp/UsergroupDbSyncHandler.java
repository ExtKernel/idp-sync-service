package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.model.Usergroup;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UsergroupDbSyncHandler {
    /**
     * Handles synchronization of user groups.
     *
     * @param differenceMap a map, which contains changes
     *                     to be applied while performing synchronization.
     */
    void sync(
            Map<String, List<Optional<Usergroup>>> differenceMap
    );
}
