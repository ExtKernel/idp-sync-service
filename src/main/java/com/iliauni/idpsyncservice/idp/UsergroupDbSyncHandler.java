package com.iliauni.idpsyncservice.idp;

import com.iliauni.idpsyncservice.model.Usergroup;

import java.util.List;
import java.util.Map;

public interface UsergroupDbSyncHandler {
    /**
     * Handles synchronization of user groups.
     *
     * @param differenceMap a map, which contains changes
     *                     to be applied while performing synchronization.
     */
    void sync(
            Map<String, List<Usergroup>> differenceMap
    );
}
