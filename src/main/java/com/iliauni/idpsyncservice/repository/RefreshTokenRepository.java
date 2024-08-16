package com.iliauni.idpsyncservice.repository;

import com.iliauni.idpsyncservice.model.RefreshToken;
import java.util.Date;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findFirstByOrderByCreationDateDesc();
    Optional<RefreshToken> findRefreshTokenByCreationDate(Date creationDate);
    void deleteRefreshTokenByCreationDateBetween(Date creationDateFrom, Date creationDateTo);
}
