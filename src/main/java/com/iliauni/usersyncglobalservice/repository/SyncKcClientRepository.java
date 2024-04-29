package com.iliauni.usersyncglobalservice.repository;

import com.iliauni.usersyncglobalservice.model.SyncKcClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SyncKcClientRepository extends JpaRepository<SyncKcClient, String> {
}
