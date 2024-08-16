package com.iliauni.idpsyncservice.difference;

import com.iliauni.idpsyncservice.model.User;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class UserListMatcher implements ListMatcher<User> {

    /***
     * @inheritDoc
     */
    @Override
    public boolean listsMatch(
            List<User> list1,
            List<User> list2
    ) {
        Set<User> set1 = new HashSet<>(list1);
        Set<User> set2 = new HashSet<>(list2);

        return set1.equals(set2);
    }
}
