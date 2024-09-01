package com.iliauni.idpsyncservice.repository;

import com.iliauni.idpsyncservice.model.UserSyncStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSyncStatusRepository extends JpaRepository<UserSyncStatus, Long> {
}
