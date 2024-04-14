package com.iliauni.usersyncglobalservice.unit.difference;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.iliauni.usersyncglobalservice.difference.UsergroupDifferenceCalculator;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class UsergroupDifferenceCalculatorTest {

    @Test
    public void calculate_WhenGivenUnequalLists_ShouldReturnDifference()
            throws Exception {
        List<Usergroup> usergroupList1 = new ArrayList<>();
        List<Usergroup> usergroupList2 = new ArrayList<>();

        Usergroup usergroup1 = new Usergroup();
        Usergroup usergroup2 = new Usergroup();

        usergroupList1.add(usergroup1);
        usergroupList2.add(usergroup2);

        MultiValueMap<String, Optional<Usergroup>> differenceMap = new LinkedMultiValueMap<>();
        differenceMap.add("new", Optional.of(usergroup2));
        differenceMap.add("missing", Optional.of(usergroup1));

        UsergroupDifferenceCalculator usergroupDifferenceCalculator = new UsergroupDifferenceCalculator();
        assertEquals(differenceMap, usergroupDifferenceCalculator.calculate(usergroupList1, usergroupList2));
    }

    @Test
    public void calculate_WhenGivenEqualLists_ShouldReturnDifference()
            throws Exception {
        List<Usergroup> usergroupList1 = new ArrayList<>();
        List<Usergroup> usergroupList2 = new ArrayList<>();

        Usergroup usergroup1 = new Usergroup();
        Usergroup usergroup2 = new Usergroup();

        usergroupList1.add(usergroup1);
        usergroupList1.add(usergroup2);
        usergroupList2.add(usergroup1);
        usergroupList2.add(usergroup2);

        UsergroupDifferenceCalculator usergroupDifferenceCalculator = new UsergroupDifferenceCalculator();
        assertEquals(new LinkedMultiValueMap<>(), usergroupDifferenceCalculator.calculate(usergroupList1, usergroupList2));
    }
}
