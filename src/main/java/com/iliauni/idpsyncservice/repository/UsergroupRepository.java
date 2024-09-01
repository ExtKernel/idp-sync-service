package com.iliauni.idpsyncservice.repository;

import com.iliauni.idpsyncservice.model.Usergroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsergroupRepository extends JpaRepository<Usergroup, String> {
    Optional<Usergroup> findUsergroupByName(String name);
    void deleteUsergroupByName(String name);
}
