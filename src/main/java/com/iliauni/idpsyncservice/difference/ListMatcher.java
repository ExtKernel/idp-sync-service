package com.iliauni.idpsyncservice.difference;

import java.util.List;

/**
 * An interface for matching lists of elements of type T.
 *
 * @param <T> the type of elements in the lists
 */
public interface ListMatcher<T> {
    /**
     * Determines whether two lists match by comparing their elements.
     *
     * @param list1 the first list to compare
     * @param list2 the second list to compare
     * @return true if the lists match, false otherwise
     */
    boolean listsMatch(
            List<T> list1,
            List<T> list2
    );
}
