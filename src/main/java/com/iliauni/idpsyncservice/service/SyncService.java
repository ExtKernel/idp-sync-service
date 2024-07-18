package com.iliauni.idpsyncservice.service;

import com.iliauni.idpsyncservice.difference.DifferenceCalculator;
import com.iliauni.idpsyncservice.exception.NoRecordOfUsergroupsException;
import com.iliauni.idpsyncservice.exception.NoRecordOfUsersException;
import com.iliauni.idpsyncservice.idp.*;
import com.iliauni.idpsyncservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SyncService {
    UsergroupService usergroupService;
    UserService userService;
    DifferenceCalculator<Usergroup> usergroupDifferenceCalculator;
    DifferenceCalculator<User> userDifferenceCalculator;
    UsergroupDbSyncHandler usergroupDbSyncHandler;
    UserDbSyncHandler userDbSyncHandler;
    CookieClientService<IpaClient> ipaCookieClientService;
    UsergroupIdpSyncHandler<IpaClient> ipaUsergroupIdpSyncHandler;
    UserIdpSyncHandler<IpaClient> ipaUserIdpSyncHandler;
    IdpUserManager<IpaClient> ipaIdpUserManager;
    Oauth2ClientService<SyncKcClient> syncKcOauth2ClientService;
    UsergroupIdpSyncHandler<SyncKcClient> syncKcUsergroupIdpSyncHandler;
    UserIdpSyncHandler<SyncKcClient> syncKcUserIdpSyncHandler;
    IdpUserManager<SyncKcClient> syncKcIdpUserManager;
    Oauth2ClientService<WinClient> winOauth2ClientService;
    UsergroupIdpSyncHandler<WinClient> winUsergroupIdpSyncHandler;
    UserIdpSyncHandler<WinClient> winUserIdpSyncHandler;
    IdpUserManager<WinClient> winIdpUserManager;

    @Autowired
    public SyncService(
            UsergroupService usergroupService, UserService userService,
            DifferenceCalculator<Usergroup> usergroupDifferenceCalculator,
            DifferenceCalculator<User> userDifferenceCalculator,
            UsergroupDbSyncHandler usergroupDbSyncHandler,
            UserDbSyncHandler userDbSyncHandler,
            CookieClientService<IpaClient> ipaCookieClientService,
            UsergroupIdpSyncHandler<IpaClient> ipaUsergroupIdpSyncHandler,
            UserIdpSyncHandler<IpaClient> ipaUserIdpSyncHandler,
            IdpUserManager<IpaClient> ipaIdpUserManager,
            Oauth2ClientService<SyncKcClient> syncKcOauth2ClientService,
            UsergroupIdpSyncHandler<SyncKcClient> syncKcUsergroupIdpSyncHandler,
            UserIdpSyncHandler<SyncKcClient> syncKcUserIdpSyncHandler,
            IdpUserManager<SyncKcClient> syncKcIdpUserManager,
            Oauth2ClientService<WinClient> winOauth2ClientService,
            UsergroupIdpSyncHandler<WinClient> winUsergroupIdpSyncHandler,
            UserIdpSyncHandler<WinClient> winUserIdpSyncHandler,
            IdpUserManager<WinClient> winIdpUserManager
    ) {
        this.usergroupService = usergroupService;
        this.userService = userService;
        this.usergroupDifferenceCalculator = usergroupDifferenceCalculator;
        this.userDifferenceCalculator = userDifferenceCalculator;
        this.usergroupDbSyncHandler = usergroupDbSyncHandler;
        this.userDbSyncHandler = userDbSyncHandler;
        this.ipaCookieClientService = ipaCookieClientService;
        this.ipaUsergroupIdpSyncHandler = ipaUsergroupIdpSyncHandler;
        this.ipaUserIdpSyncHandler = ipaUserIdpSyncHandler;
        this.ipaIdpUserManager = ipaIdpUserManager;
        this.syncKcOauth2ClientService = syncKcOauth2ClientService;
        this.syncKcUsergroupIdpSyncHandler = syncKcUsergroupIdpSyncHandler;
        this.syncKcUserIdpSyncHandler = syncKcUserIdpSyncHandler;
        this.syncKcIdpUserManager = syncKcIdpUserManager;
        this.winOauth2ClientService = winOauth2ClientService;
        this.winUsergroupIdpSyncHandler = winUsergroupIdpSyncHandler;
        this.winUserIdpSyncHandler = winUserIdpSyncHandler;
        this.winIdpUserManager = winIdpUserManager;
    }

    public void syncUsergroups(Optional<List<Usergroup>> optionalUsergroups) {
        // sync users first to not have a situation
        // that we're adding a non-existing user as a member to a user group
        syncUsersFromIdpClients(
                ipaCookieClientService,
                ipaIdpUserManager
        );
        syncUsersFromIdpClients(
                syncKcOauth2ClientService,
                syncKcIdpUserManager
        );
        syncUsersFromIdpClients(
                winOauth2ClientService,
                winIdpUserManager
        );

        optionalUsergroups.ifPresent(usergroups -> {
            List<Usergroup> dbUsergroups;

            try {
                dbUsergroups = usergroupService.findAll();
            } catch (NoRecordOfUsergroupsException exception) {
                dbUsergroups = new ArrayList<>();
            }

            Map<String, List<Optional<Usergroup>>> differenceMap = usergroupDifferenceCalculator.calculate(
                    dbUsergroups,
                    usergroups
            );

            // sync with IDPs first to not have a situation
            // that we already have updated the DB
            // and a difference calculator isn't returning any difference
            syncUsergroupsForAllIdpClients(
                    differenceMap,
                    ipaCookieClientService,
                    ipaUsergroupIdpSyncHandler
            );
            syncUsergroupsForAllIdpClients(
                    differenceMap,
                    syncKcOauth2ClientService,
                    syncKcUsergroupIdpSyncHandler
            );
            syncUsergroupsForAllIdpClients(
                    differenceMap,
                    winOauth2ClientService,
                    winUsergroupIdpSyncHandler
            );

            // sync with the local DB, when it's safe to do so and no more comparing with it is needed
            usergroupDbSyncHandler.sync(differenceMap);
        });
    }

    public void syncUsers(Optional<List<User>> optionalUsers) {
        optionalUsers.ifPresent(users -> {
            List<User> dbUsers;

            try {
                dbUsers = userService.findAll();
            } catch (NoRecordOfUsersException exception) {
                dbUsers = new ArrayList<>();
            }

            Map<String, List<Optional<User>>> differenceMap = userDifferenceCalculator.calculate(
                    dbUsers,
                    users
            );

            // sync with IDPs first to not have a situation
            // that we already have updated the DB
            // and a difference calculator isn't returning any difference
            syncUsersForAllIdpClients(
                    differenceMap,
                    ipaCookieClientService,
                    ipaUserIdpSyncHandler
            );
            syncUsersForAllIdpClients(
                    differenceMap,
                    syncKcOauth2ClientService,
                    syncKcUserIdpSyncHandler
            );
            syncUsersForAllIdpClients(
                    differenceMap,
                    winOauth2ClientService,
                    winUserIdpSyncHandler
            );

            // sync with the local DB, when it's safe to do so and no more comparing with it is needed
            userDbSyncHandler.sync(differenceMap);
        });
    }

    private <T extends Client> void syncUsergroupsForAllIdpClients(
            Map<String, List<Optional<Usergroup>>> differenceMap,
            ClientService<T> clientService,
            UsergroupIdpSyncHandler<T> usergroupIdpSyncHandler
    ) {
        clientService.findAll().forEach(client -> {
            usergroupIdpSyncHandler.sync(
                    client,
                    differenceMap
            );
        });
    }

    private <T extends Client> void syncUsersForAllIdpClients(
            Map<String, List<Optional<User>>> differenceMap,
            ClientService<T> clientService,
            UserIdpSyncHandler<T> userIdpSyncHandler
    ) {
        clientService.findAll().forEach(client -> {
            userIdpSyncHandler.sync(
                    client,
                    differenceMap
            );
        });
    }

    private <T extends Client> void syncUsersFromIdpClients(
            ClientService<T> clientService,
            IdpUserManager<T> userManager
    ) {
        clientService.findAll().forEach(client -> {
            syncUsers(Optional.of(userManager.getUsers(client)));
        });
    }
}
