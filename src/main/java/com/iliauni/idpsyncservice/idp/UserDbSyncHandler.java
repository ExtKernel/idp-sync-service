package com.iliauni.idpsyncservice.idp;

import com.iliauni.idpsyncservice.model.User;
import java.util.List;
import java.util.Map;

public interface UserDbSyncHandler {
    /**
     * Handles synchronization of user.
     *
     * @param differenceMap a map, which contains changes
     *                     to be applied while performing synchronization.
     */
    void sync(
            Map<String, List<User>> differenceMap
    );
}
