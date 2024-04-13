package com.iliauni.usersyncglobalservice.unit;

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

    @Test
    public void getListsDifference_WhenGivenDifferentLists_ShouldReturnDifference()
            throws Exception {
        List<Usergroup> usergroupList1 = new ArrayList<>();
        List<Usergroup> usergroupList2 = new ArrayList<>();

        // add the same usergroup to both lists
        // so at this point, lists are equal
        Usergroup usergroup1 = new Usergroup();
        usergroupList1.add(usergroup1);
        usergroupList2.add(usergroup1);

        // add a new usergroup to the second list
        // so lists are unequal
        Usergroup usergroup2 = new Usergroup();
        usergroupList2.add(usergroup2);

        // add the difference of lists above to a list
        List<Usergroup> differenceList = new ArrayList<>();
        differenceList.add(usergroup2);

        UsergroupListMatcher userListMatcher = new UsergroupListMatcher();
        assertEquals(differenceList, userListMatcher.getListsDifference(usergroupList1, usergroupList2));
    }

    @Test
    public void getListsDifference_WhenGivenEqualLists_ShouldReturnEmptyList()
            throws Exception {
        List<Usergroup> usergroupList1 = new ArrayList<>();
        List<Usergroup> usergroupList2 = new ArrayList<>();

        // add the same usergroup to both lists
        // so lists are equal
        Usergroup usergroup1 = new Usergroup();
        usergroupList1.add(usergroup1);
        usergroupList2.add(usergroup1);

        UsergroupListMatcher usergroupListMatcher = new UsergroupListMatcher();
        assertEquals(new ArrayList<>(), usergroupListMatcher.getListsDifference(usergroupList1, usergroupList2));
    }
}
