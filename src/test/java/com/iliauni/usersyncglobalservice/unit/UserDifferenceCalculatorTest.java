package com.iliauni.usersyncglobalservice.unit;

import com.iliauni.usersyncglobalservice.difference.UserDifferenceCalculator;
import com.iliauni.usersyncglobalservice.model.User;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDifferenceCalculatorTest {

    @Test
    public void calculate_WhenGivenUnequalLists_ShouldReturnDifference()
            throws Exception {
        List<User> userList1 = new ArrayList<>();
        List<User> userList2 = new ArrayList<>();

        User user1 = new User();
        User user2 = new User();

        userList1.add(user1);
        userList2.add(user2);

        MultivaluedMap<String, User> differenceMap = new MultivaluedHashMap<>();
        differenceMap.add("new", user2);
        differenceMap.add("missing", user1);

        UserDifferenceCalculator userDifferenceCalculator = new UserDifferenceCalculator();
        assertEquals(differenceMap, userDifferenceCalculator.calculate(userList1, userList2));
    }

    @Test
    public void calculate_WhenGivenEqualLists_ShouldReturnDifference()
            throws Exception {
        List<User> userList1 = new ArrayList<>();
        List<User> userList2 = new ArrayList<>();

        User user1 = new User();
        User user2 = new User();

        userList1.add(user1);
        userList1.add(user2);
        userList2.add(user1);
        userList2.add(user2);

        UserDifferenceCalculator userDifferenceCalculator = new UserDifferenceCalculator();
        assertEquals(new MultivaluedHashMap<>(), userDifferenceCalculator.calculate(userList1, userList2));
    }
}
