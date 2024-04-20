package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService<T extends Client> {
    T save(Optional<T> optionalClient);
    List<T> findAll();
    T findById(String id);
    T update(Optional<T> optionalClient);
    void deleteById(String id);
}
