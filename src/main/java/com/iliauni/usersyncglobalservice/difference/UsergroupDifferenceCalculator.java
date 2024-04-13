package com.iliauni.usersyncglobalservice.difference;

import com.iliauni.usersyncglobalservice.model.Usergroup;
import jakarta.ws.rs.core.MultivaluedHashMap;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsergroupDifferenceCalculator implements DifferenceCalculator<Usergroup> {

    @Override
    public MultivaluedHashMap<String, Usergroup> calculate(List<Usergroup> originalList, List<Usergroup> targetList) {
        MultivaluedHashMap<String, Usergroup> differenceMap = new MultivaluedHashMap<>();

        for (Usergroup usergroup : originalList) {
            if (originalList.contains(usergroup) && !targetList.contains(usergroup)) {
                differenceMap.add("missing", usergroup);
            }
        }
        for (Usergroup usergroup : targetList) {
            if (targetList.contains(usergroup) && !originalList.contains(usergroup)) {
                differenceMap.add("new", usergroup);
            }
        }

        return differenceMap;
    }
}
