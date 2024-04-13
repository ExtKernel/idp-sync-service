package com.iliauni.usersyncglobalservice.repository;

import com.iliauni.usersyncglobalservice.model.CookieJar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface CookieJarRepository extends JpaRepository<CookieJar, Long> {
    Optional<CookieJar> findFirstByOrderByCreationDateDesc();
    Optional<CookieJar> findCookieJarByCreationDate(Date creationDate);
    void deleteCookieJarByCreationDateBetween(Date creationDateFrom, Date creationDateTo);
}
