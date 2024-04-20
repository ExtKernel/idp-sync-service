package com.iliauni.usersyncglobalservice.repository;

import com.iliauni.usersyncglobalservice.model.Cookie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface CookieRepository extends JpaRepository<Cookie, Long> {
    Optional<Cookie> findFirstByOrderByCreationDateDesc();
    Optional<Cookie> findCookieJarByCreationDate(Date creationDate);
    void deleteCookieJarByCreationDateBetween(Date creationDateFrom, Date creationDateTo);
}
