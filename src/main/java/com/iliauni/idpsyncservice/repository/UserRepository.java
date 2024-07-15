package com.iliauni.idpsyncservice.repository;

import com.iliauni.idpsyncservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByUsername(String username);
    void deleteUserByUsername(String username);
}
