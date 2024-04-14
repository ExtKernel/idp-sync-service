package com.iliauni.usersyncglobalservice.difference;

import java.util.List;

public interface ListMatcher<T> {
    /**
     * Determines whether two lists match by comparing their elements.
     *
     * @param list1 the first list to compare
     * @param list2 the second list to compare
     * @return true if the lists match, false otherwise
     */
    boolean listsMatch(List<T> list1, List<T> list2);
}
