package com.iliauni.usersyncglobalservice.difference;

import com.iliauni.usersyncglobalservice.model.User;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

/**
 * A component class implementing the {@link DifferenceCalculator} interface for calculating the difference between two lists of {@link User} objects.
 */
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
    public MultiValueMap<String, Optional<User>> calculate(
            List<User> originalList,
            List<User> targetList
    ) {
        MultiValueMap<String, Optional<User>> differenceMap = new LinkedMultiValueMap<>();

        for (User user : originalList) {
            if (originalList.contains(user) && !targetList.contains(user)) {
                differenceMap.add("missing", Optional.ofNullable(user));
            }
        }
        for (User user : targetList) {
            if (targetList.contains(user) && !originalList.contains(user)) {
                differenceMap.add("new", Optional.ofNullable(user));
            }
        }

        return differenceMap;
    }
}
