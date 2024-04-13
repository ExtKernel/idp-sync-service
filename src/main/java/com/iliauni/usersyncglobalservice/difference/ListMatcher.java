package com.iliauni.usersyncglobalservice.difference;

import java.util.List;

public interface ListMatcher<T> {
    boolean listsMatch(List<T> list1, List<T> list2);
    List<T> getListsDifference(List<T> originalList, List<T> targetList);
}
