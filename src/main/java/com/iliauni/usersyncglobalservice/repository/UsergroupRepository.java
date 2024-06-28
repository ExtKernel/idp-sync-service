package com.iliauni.usersyncglobalservice.repository;

import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsergroupRepository extends JpaRepository<Usergroup, String> {
    Optional<Usergroup> findUsergroupByName(String name);
    void deleteUsergroupByName(String name);
}
