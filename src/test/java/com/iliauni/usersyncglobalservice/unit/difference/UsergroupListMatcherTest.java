package com.iliauni.usersyncglobalservice.unit.difference;

import com.iliauni.usersyncglobalservice.difference.UsergroupListMatcher;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsergroupListMatcherTest {
    @Test
    public void listsMatch_WhenGivenEqualLists_ShouldReturnTrue()
            throws Exception {
        List<Usergroup> usergroupList1 = new ArrayList<>();
        List<Usergroup> usergroupList2 = new ArrayList<>();

        // add the same usergroup to both lists
        // basically, make them equal
        Usergroup usergroup1 = new Usergroup();
        usergroupList1.add(usergroup1);
        usergroupList2.add(usergroup1);

        UsergroupListMatcher usergroupListMatcher = new UsergroupListMatcher();
        assertTrue(usergroupListMatcher.listsMatch(usergroupList1, usergroupList2));
    }

    @Test
    public void listsMatch_WhenGivenNotEqualLists_ShouldReturnTrue()
            throws Exception {
        List<Usergroup> usergroupList1 = new ArrayList<>();
        List<Usergroup> usergroupList2 = new ArrayList<>();

        // add an usergroup only to one of lists
        // making them unequal
        Usergroup usergroup1 = new Usergroup();
        usergroupList1.add(usergroup1);

        UsergroupListMatcher userListMatcher = new UsergroupListMatcher();
        assertFalse(userListMatcher.listsMatch(usergroupList1, usergroupList2));
    }
}
