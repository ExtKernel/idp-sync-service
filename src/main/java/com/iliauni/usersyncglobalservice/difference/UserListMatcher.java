package com.iliauni.usersyncglobalservice.difference;

import com.iliauni.usersyncglobalservice.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserListMatcher implements ListMatcher<User> {

    @Override
    public boolean listsMatch(List<User> list1, List<User> list2) {
        Set<User> set1 = new HashSet<>(list1);
        Set<User> set2 = new HashSet<>(list2);

        return set1.equals(set2);
    }

    @Override
    public List<User> getListsDifference(List<User> originalList, List<User> targetList) {
        Set<User> originalSet = new HashSet<>(originalList);
        Set<User> differentSet = new HashSet<>(targetList);

        differentSet.removeAll(originalSet);

        return new ArrayList<>(differentSet);
    }
}
