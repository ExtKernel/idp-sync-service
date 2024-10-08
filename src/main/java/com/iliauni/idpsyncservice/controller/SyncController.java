package com.iliauni.idpsyncservice.controller;

import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.model.UserSyncEvent;
import com.iliauni.idpsyncservice.model.Usergroup;
import com.iliauni.idpsyncservice.model.UsergroupSyncEvent;
import com.iliauni.idpsyncservice.service.SyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/secured/sync")
@RestController
public class SyncController {
    private final SyncService<Usergroup> usergroupSyncService;
    private final SyncService<User> userSyncService;

    @Autowired
    public SyncController(
            SyncService<Usergroup> usergroupSyncService,
            SyncService<User> userSyncService
    ) {
        this.usergroupSyncService = usergroupSyncService;
        this.userSyncService = userSyncService;
    }

    @PostMapping("/groups")
    public UsergroupSyncEvent syncUsergroups(@RequestBody List<Usergroup> usergroups) {
        return (UsergroupSyncEvent) usergroupSyncService.sync(usergroups);
    }

    @PostMapping("/users")
    public UserSyncEvent syncUsers(@RequestBody List<User> users) {
        return (UserSyncEvent) userSyncService.sync(users);
    }
}
