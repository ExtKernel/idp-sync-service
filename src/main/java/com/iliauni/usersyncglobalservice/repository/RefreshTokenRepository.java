package com.iliauni.usersyncglobalservice.repository;

import com.iliauni.usersyncglobalservice.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findFirstByOrderByCreationDateDesc();
    Optional<RefreshToken> findRefreshTokenByCreationDate(Date creationDate);
    void deleteRefreshTokenByCreationDateBetween(Date creationDateFrom, Date creationDateTo);
}
