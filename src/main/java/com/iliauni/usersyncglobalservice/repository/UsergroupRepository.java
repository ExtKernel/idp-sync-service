package com.iliauni.usersyncglobalservice.repository;

import com.iliauni.usersyncglobalservice.model.Usergroup;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsergroupRepository extends JpaRepository<Usergroup, String> {
    Optional<Usergroup> findUsergroupByName(String name);
    void deleteUsergroupByName(String name);
}
