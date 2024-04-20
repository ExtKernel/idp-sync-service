package com.iliauni.usersyncglobalservice.repository;

import com.iliauni.usersyncglobalservice.model.IpaClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IpaClientRepository extends JpaRepository<IpaClient, String> {
}
