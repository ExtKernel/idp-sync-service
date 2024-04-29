package com.iliauni.usersyncglobalservice.difference;

import java.util.Map;

import java.util.List;
import java.util.Optional;

/**
 * An interface for calculating the difference between lists of elements of type T and organizing them into a map.
 *
 * @param <T> the type of elements in the lists
 */
public interface DifferenceCalculator<T> {
    /**
     * Calculates the difference between the original list and the target list and organizes them into a multi-value map.
     * Each key in the map represents a category of difference, and the corresponding value is a list of optional elements.
     *
     * @param originalList the original list
     * @param targetList the target list to compare against
     * @return a map containing list representing the differences between the original and target lists
     *         organized by categories, where each category key is mapped to a list of optional elements
     */
    Map<String, List<Optional<T>>> calculate(
            List<T> originalList,
            List<T> targetList
    );
}
