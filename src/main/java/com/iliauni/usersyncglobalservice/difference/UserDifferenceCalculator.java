package com.iliauni.usersyncglobalservice.difference;

import com.iliauni.usersyncglobalservice.model.User;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

@Component
public class UserDifferenceCalculator implements DifferenceCalculator<User> {

    @Override
    public MultiValueMap<String, Optional<User>> calculate(List<User> originalList, List<User> targetList) {
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
