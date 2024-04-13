package com.iliauni.usersyncglobalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository<T> extends JpaRepository<T, String> {
    Optional<T> findClientById(String id);
    void deleteClientById(String id);
}
