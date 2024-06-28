package com.iliauni.usersyncglobalservice.difference;

import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * A component class implementing the {@link DifferenceCalculator} interface
 * for calculating the difference between two lists of {@link Usergroup} objects.
 */
@Component
public class UsergroupDifferenceCalculator implements DifferenceCalculator<Usergroup> {
    private final ListMatcher<User> userListMatcher;

    @Autowired
    public UsergroupDifferenceCalculator(ListMatcher<User> userListMatcher) {
        this.userListMatcher = userListMatcher;
    }

    /**
     * @inheritDoc
     *
     * If a {@link Usergroup} object exists in the original list
     * but not in the target list, it will be mapped to the "missing" key.
     * If a {@link Usergroup} object exists in the target list
     * but not in the original list, it will be mapped to the "new" key.
     * If a {@link Usergroup} object exists in both lists, and they have different user lists
     * it will be mapped to the "altered" key.
     * If a {@link Usergroup} object exists in both lists and doesn't differ,
     * it will not be present in the map.
     */
    @Override
    public Map<String, List<Optional<Usergroup>>> calculate(
            List<Usergroup> originalList,
            List<Usergroup> targetList
    ) {
        Map<String, List<Optional<Usergroup>>> differenceMap = new HashMap<>();

        differenceMap.put("missing", calculateMissingUsergroups(
                originalList,
                targetList
        ));

        differenceMap.put("new", calculateNewUsergroups(
                originalList,
                targetList
        ));

        differenceMap.put("altered", calculateAlteredUsergroups(
                originalList,
                targetList
        ));

        return differenceMap;
    }

    private List<Optional<Usergroup>> calculateMissingUsergroups(
            List<Usergroup> originalList,
            List<Usergroup> targetList
    ) {
        List<Optional<Usergroup>> missingUsergroups = new ArrayList<>();

        // append missing usergroups
        originalList.stream()
                .filter(usergroup -> !targetList.contains(usergroup))
                .forEach(usergroup -> missingUsergroups.add(Optional.of(usergroup)));

        return missingUsergroups;
    }

    private List<Optional<Usergroup>> calculateNewUsergroups(
            List<Usergroup> originalList,
            List<Usergroup> targetList
    ) {
        List<Optional<Usergroup>> newUsergroups = new ArrayList<>();

        // append new usergroups
        targetList.stream()
                .filter(usergroup -> !originalList.contains(usergroup))
                .forEach(usergroup -> newUsergroups.add(Optional.of(usergroup)));

        return newUsergroups;
    }

    private List<Optional<Usergroup>> calculateAlteredUsergroups(
            List<Usergroup> originalList,
            List<Usergroup> targetList
    ) {
        List<Optional<Usergroup>> alteredUsergroups = new ArrayList<>();

        // append altered usergroups
        targetList.stream()
                .filter(originalList::contains)
                .filter(usergroup -> {
                    List<User> originalUsergroupMembers = originalList.get(originalList.indexOf(usergroup)).getUsers();
                    // Null-check original Usergroup user list
                    // Create an empty list, if user list is null
                    if (originalUsergroupMembers == null) {
                        originalUsergroupMembers = new ArrayList<>();
                    }

                    List<User> targetUsergroupMembers = targetList.get(targetList.indexOf(usergroup)).getUsers();
                    // Null-check target Usergroup user list
                    // Create an empty list, if user list is null
                    if (targetUsergroupMembers == null) {
                        targetUsergroupMembers = new ArrayList<>();
                    }

                    return !userListMatcher.listsMatch(
                            originalUsergroupMembers,
                            targetUsergroupMembers
                    );
                })
                .forEach(usergroup -> alteredUsergroups.add(Optional.of(usergroup)));

        return alteredUsergroups;
    }
}
