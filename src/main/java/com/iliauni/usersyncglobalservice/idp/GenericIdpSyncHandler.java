package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.difference.DifferenceCalculator;
import com.iliauni.usersyncglobalservice.model.Client;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import com.iliauni.usersyncglobalservice.service.UserService;
import com.iliauni.usersyncglobalservice.service.UsergroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class GenericIdpSyncHandler<T extends Client> implements IdpSyncHandler<T> {
    private final IdpUserManager<T> userManager;
    private final IdpUsergroupManager<T> usergroupManager;
    private final UsergroupService usergroupService;
    private final UserService userService;
    private final DifferenceCalculator<User> userDifferenceCalculator;

    @Autowired
    public GenericIdpSyncHandler(
            IdpUserManager<T> userManager,
            @Lazy IdpUsergroupManager<T> usergroupManager,
            UsergroupService usergroupService,
            UserService userService,
            DifferenceCalculator<User> userDifferenceCalculator
    ) {
        this.userManager = userManager;
        this.usergroupManager = usergroupManager;
        this.usergroupService = usergroupService;
        this.userService = userService;
        this.userDifferenceCalculator = userDifferenceCalculator;
    }

    @Override
    public void syncUsergroupChanges(
            T client,
            Map<String, List<Optional<Usergroup>>> differenceMap
    ) {
        buildSyncFlagsMap().forEach((key, flags) -> {
            Boolean isNew = flags[0];
            Boolean isAltered = flags[1];

            forceUsergroupChanges(
                    client,
                    differenceMap.getOrDefault(key, new ArrayList<>()),
                    isNew,
                    isAltered
            );
        });
    }

    @Override
    public void forceUsergroupChanges(
            T client,
            List<Optional<Usergroup>> usergroups,
            boolean isNew,
            boolean isAltered
    ) {
        forceUsergroupChangesOnLocalDb(
                usergroups,
                isNew,
                isAltered
        );

        forceUsergroupChangesOnIdp(
                client,
                usergroups,
                isNew,
                isAltered
        );
    }

    @Override
    public void forceUserChanges(
            T client,
            List<Optional<User>> users,
            boolean isNew
    ) {
        forceUserChangesOnLocalDb(
                users,
                isNew
        );

        forceUserChangesOnIdp(
                client,
                users,
                isNew
        );
    }

    private void forceUsergroupUserChanges(
            T client,
            Optional<Usergroup> optionalUsergroup,
            List<Optional<User>> users,
            boolean isNew
    ) {
        forceUserChanges(
                client,
                users,
                isNew
        );

        forceUsergroupUsersChangesOnIdp(
                client,
                optionalUsergroup,
                users,
                isNew
        );
    }

    /**
     * Forces(makes) the changes on the local DB using UsergroupService.
     *
     * @param usergroups a list of user groups, which are, basically, the change to be made.
     * @param isNew if true, user groups will be saved.
     *             If false, while "isAltered" is also false, user groups will be deleted.
     * @param isAltered if true, user groups will be updated.
     *                 If false, while "isNew" is also false, user groups will be deleted.
     */
    private void forceUsergroupChangesOnLocalDb(
            List<Optional<Usergroup>> usergroups,
            boolean isNew,
            boolean isAltered
    ) {
        usergroups.forEach(usergroup -> {
            usergroup.ifPresent(u -> {
                if (isNew) {
                    usergroupService.save(Optional.of(u));
                } else if (isAltered) {
                    usergroupService.update(Optional.of(u));
                } else {
                    usergroupService.deleteByName(u.getName());
                }
            });
        });
    }

    /**
     * Forces(makes) the changes on the IDP client using UsergroupManager.
     *
     * @param usergroups a list of user groups, which are, basically, the change to be made.
     * @param isNew if true, user groups will be created.
     *             If false, while "isAltered" is also false, user groups will be deleted.
     * @param isAltered if true, user group's users will be updated.
     *                 If false, while "isNew" is also false, user groups will be deleted.
     */
    private void forceUsergroupChangesOnIdp(
            T client,
            List<Optional<Usergroup>> usergroups,
            boolean isNew,
            boolean isAltered
    ) {
        usergroups.forEach(usergroup -> {
            usergroup.ifPresent(u -> {
                if (isNew) {
                    usergroupManager.createUsergroup(client, u);
                } else if (isAltered) {
                    Map<String, List<Optional<User>>> userDifferenceMap = userDifferenceCalculator.calculate(
                            usergroupService.findByName(u.getName()).getUsers(),
                            u.getUsers()
                    );

                    buildSyncFlagsMap()
                            .entrySet()
                            .stream()
                            // filter out "altered" as a user can't have such state
                            .filter(entry -> !entry.getKey().equals("altered"))
                            .forEach(entry -> {
                                String entryKey = entry.getKey();
                                Boolean isUserNew = entry.getValue()[0];

                                forceUsergroupUserChanges(
                                        client,
                                        Optional.of(u),
                                        userDifferenceMap.getOrDefault(entryKey, new ArrayList<>()),
                                        isUserNew
                                );
                            });
                } else {
                    usergroupManager.deleteUsergroup(client, u.getName());
                }
            });
        });
    }

    /**
     * Forces(makes) the changes on the local DB using UserService.
     *
     * @param users a list of users, which are, basically, the change to be made.
     * @param isNew if true, users will be saved.
     *             If false, users will be deleted.
     */
    private void forceUserChangesOnLocalDb(
            List<Optional<User>> users,
            boolean isNew
    ) {
        users.forEach(user -> {
            user.ifPresent(u -> {
                if (isNew) {
                    userService.save(Optional.of(u));
                } else {
                    userService.deleteByUsername(u.getUsername());
                }
            });
        });
    }

    /**
     * Forces(makes) the changes on the IDP client using UserService.
     *
     * @param users a list of users, which are, basically, the change to be made.
     * @param isNew if true, users will be created.
     *             If false, users will be deleted.
     */
    private void forceUserChangesOnIdp(
            T client,
            List<Optional<User>> users,
            boolean isNew
    ) {
        users.forEach(user -> {
            user.ifPresent(u -> {
                if (isNew) {
                    userManager.createUser(client, u);
                } else {
                    userManager.deleteUser(client, u.getUsername());
                }
            });
        });
    }

    private void forceUsergroupUsersChangesOnIdp(
            T client,
            Optional<Usergroup> usergroup,
            List<Optional<User>> users,
            boolean isNew
    ) {
        usergroup.ifPresent(group -> {
            users.forEach(user -> {
                user.ifPresent(u -> {
                    if (isNew) {
                        usergroupManager.addUsergroupMember(
                                client,
                                group.getName(),
                                u.getUsername()
                        );
                    } else {
                        usergroupManager.removeUsergroupMember(
                                client,
                                group.getName(),
                                u.getUsername()
                        );
                    }
                });
            });
        });
    }

    /**
     * Builds a map containing boolean representations of flags,
     * which are supposed to be passed to synchronization methods.
     * Such as: "new", "altered", "missing", etc.
     * The keys are meant to match the keys of difference map, generated by DifferenceCalculator.
     * This exists for the sake of reducing boilerplate and adding some scalability.
     *
     * @return a map of boolean representations of DifferenceCalculator map's flags
     */
    private Map<String, Boolean[]> buildSyncFlagsMap() {
        return Map.of(
                "new", new Boolean[]{true, false},
                "altered", new Boolean[]{false, true},
                "missing", new Boolean[]{false, false}
        );
    }
}
