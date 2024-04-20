package com.iliauni.usersyncglobalservice.difference;

import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

/**
 * A component class implementing the {@link DifferenceCalculator} interface for calculating the difference between two lists of {@link Usergroup} objects.
 */
@Component
public class UsergroupDifferenceCalculator implements DifferenceCalculator<Usergroup> {

    /**
     * @inheritDoc
     *
     * If a {@link Usergroup} object exists in the original list but not in the target list, it will be mapped to the "missing" key.
     * If a {@link Usergroup} object exists in the target list but not in the original list, it will be mapped to the "new" key.
     * If a {@link Usergroup} object exists in both lists, it will not be present in the map.
     *
     * @param originalList the original list of {@link Usergroup} objects
     * @param targetList the target list of {@link Usergroup} objects to compare against
     * @return a multi-value map containing the differences between the original and target lists
     *         organized by categories, where each category key is mapped to a list of optional {@link Usergroup} objects
     */
    @Override
    public MultiValueMap<String, Optional<Usergroup>> calculate(
            List<Usergroup> originalList,
            List<Usergroup> targetList
    ) {
        MultiValueMap<String, Optional<Usergroup>> differenceMap = new LinkedMultiValueMap<>();

        for (Usergroup usergroup : originalList) {
            if (originalList.contains(usergroup) && !targetList.contains(usergroup)) {
                differenceMap.add("missing", Optional.ofNullable(usergroup));
            }
        }
        for (Usergroup usergroup : targetList) {
            if (targetList.contains(usergroup) && !originalList.contains(usergroup)) {
                differenceMap.add("new", Optional.ofNullable(usergroup));
            }
        }

        return differenceMap;
    }
}
