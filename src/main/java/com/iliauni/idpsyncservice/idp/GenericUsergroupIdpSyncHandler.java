package com.iliauni.idpsyncservice.idp;

import com.iliauni.idpsyncservice.difference.DifferenceCalculator;
import com.iliauni.idpsyncservice.model.Client;
import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.model.Usergroup;
import com.iliauni.idpsyncservice.service.UsergroupService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * An abstract class implementing {@link UsergroupIdpSyncHandler} for synchronizing user groups
 * in the Identity Provider (IDP) context.
 *
 * @param <T> the IDP client type. Defines synchronization specifics.
 */
@Slf4j
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
            Map<String, List<Usergroup>> differenceMap
    ) {
        buildSyncFlagsMap().forEach((entryKey, value) -> {
            Boolean isNew = value[0];
            Boolean isAltered = value[1];

            forceUsergroupChangesOnIdp(
                    client,
                    differenceMap.getOrDefault(entryKey, new ArrayList<>()),
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
            List<Usergroup> usergroups,
            boolean isNew,
            boolean isAltered
    ) {
        List<CompletableFuture<Void>> voidFutures = new ArrayList<>();

        usergroups.parallelStream().forEach(usergroup -> {
            if (isNew) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    usergroupManager.createUsergroup(
                            client,
                            usergroup,
                            true
                    );

                    if (!usergroup.getUsers().isEmpty()) {
                        forceUsergroupMembersChangesOnIdp(
                                client,
                                usergroup,
                                usergroup.getUsers(),
                                true
                        );
                    }
                });

                voidFutures.add(future);
            } else if (isAltered) {
                Map<String, List<User>> userDifferenceMap = userDifferenceCalculator
                        .calculate(
                                usergroupService.findById(usergroup.getName()).getUsers(),
                                usergroup.getUsers()
                        );

                buildSyncFlagsMap().entrySet().stream()
                        .filter(entry -> !entry.getKey().equals("altered"))
                        .forEach(entry -> {
                            String entryKey = entry.getKey();
                            Boolean isUserNew = entry.getValue()[0];

                            CompletableFuture<Void> future = CompletableFuture.runAsync(() ->
                                    forceUsergroupMembersChangesOnIdp(
                                            client,
                                            usergroup,
                                            userDifferenceMap.getOrDefault(entryKey, new ArrayList<>()),
                                            isUserNew
                                    )
                            );
                            voidFutures.add(future);
                        });

            } else {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() ->
                        usergroupManager.deleteUsergroup(
                                client,
                                usergroup.getName(),
                                true
                        )
                );
                voidFutures.add(future);
            }
        });

        try {
            // wait for all futures to complete
            CompletableFuture.allOf(voidFutures.toArray(new CompletableFuture[0])).join();
        } catch (CancellationException | CompletionException exception) {
            log.error(
                    "Error occurred while forcing user group changes on IDP" +
                            " asynchronously for a client with id "
                            + client.getId(),
                    exception
            );
            throw exception;
        }
    }

    /**
     * Forces(makes) the changes on an IDP client using UsergroupManager,
     * synchronizing user group members.
     *
     * @param client the client to perform synchronization on.
     * @param usergroup the user group to synchronize members of.
     * @param users a list of users to be synchronized.
     * @param isNew if true, a user will be added to the user group as a member.
     *             If false, a member(user) will be removed from the user group.
     */
    private void forceUsergroupMembersChangesOnIdp(
            T client,
            Usergroup usergroup,
            List<User> users,
            boolean isNew
    ) {
        List<CompletableFuture<Void>> voidFutures = new ArrayList<>();

        users.forEach(user -> {
            if (isNew) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() ->
                        usergroupManager.addUsergroupMember(
                                client,
                                usergroup.getName(),
                                user.getUsername(),
                                true
                        )
                );
                voidFutures.add(future);
            } else {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() ->
                        usergroupManager.removeUsergroupMember(
                                client,
                                usergroup.getName(),
                                user.getUsername(),
                                true
                        )
                );
                voidFutures.add(future);
            }
        });

        try {
            // wait for all futures to complete
            CompletableFuture.allOf(voidFutures.toArray(new CompletableFuture[0])).join();
        } catch (CancellationException | CompletionException exception) {
            log.error(
                    "Error occurred while forcing user group member changes on IDP" +
                            " asynchronously for a client with id "
                            + client.getId(),
                    exception
            );
            throw exception;
        }
    }

    /**
     * Builds a map containing boolean representations of flags,
     * which are supposed to be passed to synchronization methods.
     * Such as "new", "altered", "missing", etc.
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
