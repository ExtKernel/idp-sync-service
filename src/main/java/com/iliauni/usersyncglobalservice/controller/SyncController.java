package com.iliauni.usersyncglobalservice.controller;

import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import com.iliauni.usersyncglobalservice.service.SyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequestMapping("/secured/sync")
@RestController
public class SyncController {
    SyncService service;

    @Autowired
    public SyncController(SyncService service) {
        this.service = service;
    }

    @PostMapping("/usergroups")
    public void syncUsergroups(@RequestBody List<Usergroup> usergroups) {
        service.syncUsergroups(Optional.of(usergroups));
    }

    @PostMapping("/users")
    public void syncUsers(@RequestBody List<User> users) {
        service.syncUsers(Optional.of(users));
    }
}
