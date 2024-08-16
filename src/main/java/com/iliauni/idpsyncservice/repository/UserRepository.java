package com.iliauni.idpsyncservice.repository;

import com.iliauni.idpsyncservice.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByUsername(String username);
    void deleteUserByUsername(String username);
}
