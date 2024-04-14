package com.iliauni.usersyncglobalservice.difference;

import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

@Component
public class UsergroupDifferenceCalculator implements DifferenceCalculator<Usergroup> {

    @Override
    public MultiValueMap<String, Optional<Usergroup>> calculate(List<Usergroup> originalList, List<Usergroup> targetList) {
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
