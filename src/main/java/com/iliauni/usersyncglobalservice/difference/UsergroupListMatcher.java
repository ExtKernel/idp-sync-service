package com.iliauni.usersyncglobalservice.difference;

import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UsergroupListMatcher implements ListMatcher<Usergroup> {

    /***
     * @inheritDoc
     */
    @Override
    public boolean listsMatch(
            List<Usergroup> list1,
            List<Usergroup> list2
    ) {
        Set<Usergroup> set1 = new HashSet<>(list1);
        Set<Usergroup> set2 = new HashSet<>(list2);

        return set1.equals(set2);
    }
}
