package com.iliauni.idpsyncservice.difference;

import com.iliauni.idpsyncservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A component class implementing the {@link DifferenceCalculator} interface for calculating the difference between two lists of {@link User} objects.
 */
@Slf4j
@Component
public class UserDifferenceCalculator implements DifferenceCalculator<User> {

    /**
     * @inheritDoc
     *
     * If a {@link User} object exists in the original list but not in the target list, it will be mapped to the "missing" key.
     * If a {@link User} object exists in the target list but not in the original list, it will be mapped to the "new" key.
     * If a {@link User} object exists in both lists, it will not be present in the map.
     *
     * @param originalList the original list of {@link User} objects
     * @param targetList the target list of {@link User} objects to compare against
     * @return a multi-value map containing the differences between the original and target lists
     *         organized by categories, where each category key is mapped to a list of optional {@link User} objects
     */
    @Override
    public Map<String, List<User>> calculate(
            List<User> originalList,
            List<User> targetList
    ) {
        Map<String, List<User>> differenceMap = new HashMap<>();

        differenceMap.put("missing", calculateMissingUsers(
                originalList,
                targetList
        ));

        differenceMap.put("new", calculateNewUsers(
                originalList,
                targetList
        ));

        return differenceMap;
    }

    private List<User> calculateMissingUsers(
            List<User> originalList,
            List<User> targetList
    ) {
        List<User> missingUsers = new ArrayList<>();

        // append missing users
        originalList.stream()
                .filter(user -> !targetList.contains(user))
                .forEach(missingUsers::add);

        return missingUsers;
    }

    private List<User> calculateNewUsers(
            List<User> originalList,
            List<User> targetList
    ) {
        List<User> newUsers = new ArrayList<>();

        targetList.stream()
                .filter(user -> !originalList.contains(user))
                .forEach(newUsers::add);

        return newUsers;
    }
}
