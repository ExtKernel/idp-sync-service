package com.iliauni.usersyncglobalservice.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.iliauni.usersyncglobalservice.difference.UsergroupDifferenceCalculator;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

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

        MultivaluedMap<String, Usergroup> differenceMap = new MultivaluedHashMap<>();
        differenceMap.add("new", usergroup2);
        differenceMap.add("missing", usergroup1);

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
        assertEquals(new MultivaluedHashMap<>(), usergroupDifferenceCalculator.calculate(usergroupList1, usergroupList2));
    }
}
