package com.iliauni.idpsyncservice.repository;

import com.iliauni.idpsyncservice.model.UserSyncEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSyncEventRepository extends JpaRepository<UserSyncEvent, Long> {
}
