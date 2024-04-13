package com.iliauni.usersyncglobalservice.repository;

import com.iliauni.usersyncglobalservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByUsername(String username);
    void deleteUserByUsername(String username);
}
