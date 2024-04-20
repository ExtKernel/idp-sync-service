package com.iliauni.usersyncglobalservice.repository;

import com.iliauni.usersyncglobalservice.model.WinClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WinClientRepository extends JpaRepository<WinClient, String> {
}
