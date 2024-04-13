package com.iliauni.usersyncglobalservice.difference;

import com.iliauni.usersyncglobalservice.model.User;
import jakarta.ws.rs.core.MultivaluedHashMap;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDifferenceCalculator implements DifferenceCalculator<User> {

    @Override
    public MultivaluedHashMap<String, User> calculate(List<User> originalList, List<User> targetList) {
        MultivaluedHashMap<String, User> differenceMap = new MultivaluedHashMap<>();

        for (User user : originalList) {
            if (originalList.contains(user) && !targetList.contains(user)) {
                differenceMap.add("missing", user);
            }
        }
        for (User user : targetList) {
            if (targetList.contains(user) && !originalList.contains(user)) {
                differenceMap.add("new", user);
            }
        }

        return differenceMap;
    }
}
