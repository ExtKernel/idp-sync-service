package com.iliauni.idpsyncservice.repository;

import com.iliauni.idpsyncservice.model.UsergroupSyncEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsergroupSyncEventRepository extends JpaRepository<UsergroupSyncEvent, Long> {
}
