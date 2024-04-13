package com.iliauni.usersyncglobalservice.difference;

import jakarta.ws.rs.core.MultivaluedHashMap;

import java.util.List;

public interface DifferenceCalculator<T> {
    MultivaluedHashMap<String, T> calculate(List<T> originalList, List<T> targetList);
}
