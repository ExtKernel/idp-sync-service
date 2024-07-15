package com.iliauni.idpsyncservice.repository;

import com.iliauni.idpsyncservice.model.SyncKcClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SyncKcClientRepository extends JpaRepository<SyncKcClient, String> {
}
