package com.iliauni.usersyncglobalservice.difference;

import org.springframework.util.MultiValueMap;

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
     * @return a multi-value map containing the differences between the original and target lists
     *         organized by categories, where each category key is mapped to a list of optional elements
     */
    MultiValueMap<String, Optional<T>> calculate(
            List<T> originalList,
            List<T> targetList
    );
}
