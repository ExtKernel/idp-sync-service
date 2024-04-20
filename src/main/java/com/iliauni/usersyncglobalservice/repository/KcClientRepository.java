package com.iliauni.usersyncglobalservice.repository;

import com.iliauni.usersyncglobalservice.model.KcClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KcClientRepository extends JpaRepository<KcClient, String> {
}
