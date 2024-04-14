package com.iliauni.usersyncglobalservice.unit.difference;

import com.iliauni.usersyncglobalservice.difference.UserDifferenceCalculator;
import com.iliauni.usersyncglobalservice.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        MultiValueMap<String, Optional<User>> differenceMap = new LinkedMultiValueMap<>();
        differenceMap.add("new", Optional.of(user2));
        differenceMap.add("missing", Optional.of(user1));

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
        assertEquals(new LinkedMultiValueMap<>(), userDifferenceCalculator.calculate(userList1, userList2));
    }
}
