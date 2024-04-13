package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.difference.UserDifferenceCalculator;
import com.iliauni.usersyncglobalservice.difference.UsergroupDifferenceCalculator;
import com.iliauni.usersyncglobalservice.exception.UsergroupIsNullException;
import com.iliauni.usersyncglobalservice.difference.UserListMatcher;
import com.iliauni.usersyncglobalservice.difference.UsergroupListMatcher;
import com.iliauni.usersyncglobalservice.idp.IdpUserRequestSender;
import com.iliauni.usersyncglobalservice.idp.IdpUsergroupRequestSender;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.model.KcClient;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import jakarta.ws.rs.core.MultivaluedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SyncService {
    @Lazy
    private UsergroupService usergroupService;

    @Lazy
    private UserService userService;

    @Lazy
    private KcClientService<KcClient> kcClientService;

    @Lazy
    private IpaClientService<IpaClient> ipaClientService;

    private final UsergroupListMatcher usergroupListMatcher;
    private final UserListMatcher userListMatcher;
    private final UsergroupDifferenceCalculator usergroupDifferenceCalculator;
    private final UserDifferenceCalculator userDifferenceCalculator;
    private IdpUserRequestSender<KcClient> kcIdpUserRequestSender;
    private IdpUsergroupRequestSender<KcClient> kcIdpUsergroupRequestSender;
    private IdpUserRequestSender<IpaClient> ipaIdpUserRequestSender;
    private IdpUsergroupRequestSender<IpaClient> ipaIdpUsergroupRequestSender;

    @Autowired
    public SyncService(
            UsergroupService usergroupService,
            UsergroupListMatcher usergroupListMatcher,
            UserListMatcher userListMatcher,
            UsergroupDifferenceCalculator usergroupDifferenceCalculator,
            UserDifferenceCalculator userDifferenceCalculator) {
        this.usergroupService = usergroupService;
        this.usergroupListMatcher = usergroupListMatcher;
        this.userListMatcher = userListMatcher;
        this.usergroupDifferenceCalculator = usergroupDifferenceCalculator;
        this.userDifferenceCalculator = userDifferenceCalculator;
    }

    public void sync(Optional<List<Usergroup>> usergroups) {
        List<Usergroup> requestUsergroups = usergroups.orElseThrow(() -> new UsergroupIsNullException("User group is not present"));

        syncUsergroups(requestUsergroups);
        syncUsergroupUsers(requestUsergroups);
    }

    private void syncUsergroups(List<Usergroup> requestUsergroups) {
        List<Usergroup> dbUsergroups = usergroupService.findAll();

        if (!usergroupListMatcher.listsMatch(dbUsergroups, requestUsergroups)) {
            MultivaluedHashMap<String, Usergroup> differenceMap = usergroupDifferenceCalculator.calculate(dbUsergroups, requestUsergroups);

            List<KcClient> kcClients = kcClientService.findAll();
            List<IpaClient> ipaClients = ipaClientService.findAll();

            for (Usergroup newUsergroup : differenceMap.getOrDefault("new", null)) {
                for (KcClient kcClient : kcClients) {
                    kcIdpUsergroupRequestSender.createUsergroup(kcClient, newUsergroup);
                }

                for (IpaClient ipaClient : ipaClients) {
                    ipaIdpUsergroupRequestSender.createUsergroup(ipaClient, newUsergroup);
                }

                usergroupService.save(Optional.of(newUsergroup));
            }
            for (Usergroup missingUsergroup : differenceMap.getOrDefault("missing", null)) {
                for (KcClient kcClient : kcClients) {
                    kcIdpUsergroupRequestSender.deleteUsergroup(kcClient, missingUsergroup.getName());
                }

                for (IpaClient ipaClient : ipaClients) {
                    ipaIdpUsergroupRequestSender.deleteUsergroup(ipaClient, missingUsergroup.getName());
                }

                usergroupService.deleteByName(missingUsergroup.getName());
            }
        }
    }

    private void syncUsergroupUsers(List<Usergroup> requestUsergroups) {
        for (Usergroup usergroup : requestUsergroups) {
            List<User> dbUsergroupUsers = usergroupService.findByName(usergroup.getName()).getUsers();

            if (!userListMatcher.listsMatch(dbUsergroupUsers, usergroup.getUsers())) {
                MultivaluedHashMap<String, User> differenceMap = userDifferenceCalculator.calculate(dbUsergroupUsers, usergroup.getUsers());

                List<KcClient> kcClients = kcClientService.findAll();
                List<IpaClient> ipaClients = ipaClientService.findAll();

                for (User newUser : differenceMap.getOrDefault("new", null)) {
                    for (KcClient kcClient : kcClients) {
                        kcIdpUserRequestSender.createUser(kcClient, newUser);
                    }

                    for (IpaClient ipaClient : ipaClients) {
                        ipaIdpUserRequestSender.createUser(ipaClient, newUser);
                    }
                }
                for (User missingUser : differenceMap.getOrDefault("missing", null)) {
                    for (KcClient kcClient : kcClients) {
                        kcIdpUserRequestSender.deleteUser(kcClient, missingUser.getUsername());
                    }

                    for (IpaClient ipaClient : ipaClients) {
                        ipaIdpUserRequestSender.deleteUser(ipaClient, missingUser.getUsername());
                    }

                    userService.deleteByUsername(missingUser.getUsername());
                }
            }
        }
    }
}
