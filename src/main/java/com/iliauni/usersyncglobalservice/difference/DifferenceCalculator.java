package com.iliauni.usersyncglobalservice.difference;

import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

public interface DifferenceCalculator<T> {
    MultiValueMap<String, Optional<T>> calculate(List<T> originalList, List<T> targetList);
}
