package com.iliauni.idpsyncservice.idp;

import com.iliauni.idpsyncservice.difference.DifferenceCalculator;
import com.iliauni.idpsyncservice.model.Client;
import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.model.Usergroup;
import com.iliauni.idpsyncservice.service.UsergroupService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * An abstract class implementing {@link UsergroupIdpSyncHandler} for synchronizing user groups
 * in an Identity Provider (IDP) context.
 * The class itself is abstract, because it can't operate without a specific client type
 * due to the issue of autowiring generic classes.
 * A client type is supposed to be specified concretely while inheriting!!!
 * Do not use a <T extend SomeClass> or any other generic format,
 * while inheriting as a non-abstract class. There's just no point in doing that.
 *
 * @param <T> an IDP client type. Defines synchronization specifics.
 */
public abstract class GenericUsergroupIdpSyncHandler<T extends Client>
        implements UsergroupIdpSyncHandler<T> {
    private final UsergroupService usergroupService;
    private final IdpUsergroupManager<T> usergroupManager;
    private final DifferenceCalculator<User> userDifferenceCalculator;

    protected GenericUsergroupIdpSyncHandler(
            UsergroupService usergroupService,
            IdpUsergroupManager<T> usergroupManager,
            DifferenceCalculator<User> userDifferenceCalculator
    ) {
        this.usergroupService = usergroupService;
        this.usergroupManager = usergroupManager;
        this.userDifferenceCalculator = userDifferenceCalculator;
    }

    @Override
    public void sync(
            T client,
            Map<String, List<Optional<Usergroup>>> differenceMap
    ) {
        buildSyncFlagsMap().forEach((key, flags) -> {
            Boolean isNew = flags[0];
            Boolean isAltered = flags[1];

            forceUsergroupChangesOnIdp(
                    client,
                    differenceMap.getOrDefault(key, new ArrayList<>()),
                    isNew,
                    isAltered
            );
        });
    }

    /**
     * Forces(makes) the changes on an IDP client using UsergroupManager,
     * synchronizing user groups and user group members.
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
                    usergroupManager.createUsergroup(client, u, true);
                } else if (isAltered) {
                    Map<String, List<Optional<User>>> userDifferenceMap = userDifferenceCalculator
                            .calculate(
                            usergroupService.findById(u.getName()).getUsers(),
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

                                forceUsergroupMembersChangesOnIdp(
                                        client,
                                        Optional.of(u),
                                        userDifferenceMap.getOrDefault(entryKey, new ArrayList<>()),
                                        isUserNew
                                );
                            });
                } else {
                    usergroupManager.deleteUsergroup(client, u.getName(), true);
                }
            });
        });
    }

    /**
     * Forces(makes) the changes on an IDP client using UsergroupManager,
     * synchronizing user group members.
     *
     * @param client a client to perform synchronization on.
     * @param usergroup a user group to synchronize members of.
     * @param users a list of users to be synchronized.
     * @param isNew if true, a user will be added to the user group as a member.
     *             If false, a member(user) will be removed from the user group.
     */
    private void forceUsergroupMembersChangesOnIdp(
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
                                u.getUsername(),
                                true
                        );
                    } else {
                        usergroupManager.removeUsergroupMember(
                                client,
                                group.getName(),
                                u.getUsername(),
                                true
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
