package com.iliauni.usersyncglobalservice.unit.difference;

import com.iliauni.usersyncglobalservice.difference.UserListMatcher;
import com.iliauni.usersyncglobalservice.model.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserListMatcherTest {

    @Test
    public void listsMatch_WhenGivenEqualLists_ShouldReturnTrue()
            throws Exception {
        List<User> userList1 = new ArrayList<>();
        List<User> userList2 = new ArrayList<>();

        // add the same user to both lists
        // basically, make them equal
        User user1 = new User();
        userList1.add(user1);
        userList2.add(user1);

        UserListMatcher userListMatcher = new UserListMatcher();
        assertTrue(userListMatcher.listsMatch(userList1, userList2));
    }

    @Test
    public void listsMatch_WhenGivenNotEqualLists_ShouldReturnTrue()
            throws Exception {
        List<User> userList1 = new ArrayList<>();
        List<User> userList2 = new ArrayList<>();

        // add a user only to one of lists
        // making them unequal
        User user1 = new User();
        userList1.add(user1);

        UserListMatcher userListMatcher = new UserListMatcher();
        assertFalse(userListMatcher.listsMatch(userList1, userList2));
    }
}
