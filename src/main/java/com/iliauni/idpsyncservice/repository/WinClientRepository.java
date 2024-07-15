package com.iliauni.idpsyncservice.repository;

import com.iliauni.idpsyncservice.model.WinClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WinClientRepository extends JpaRepository<WinClient, String> {
}
