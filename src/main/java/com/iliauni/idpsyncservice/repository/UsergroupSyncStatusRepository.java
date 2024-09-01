package com.iliauni.idpsyncservice.repository;

import com.iliauni.idpsyncservice.model.UsergroupSyncStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsergroupSyncStatusRepository extends JpaRepository<UsergroupSyncStatus, Long> {
}
