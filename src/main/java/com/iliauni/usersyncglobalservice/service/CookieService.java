package com.iliauni.usersyncglobalservice.service;

import java.util.Date;
import java.util.Optional;

import com.iliauni.usersyncglobalservice.cookiejar.IpaCookieJarRetriever;
import com.iliauni.usersyncglobalservice.exception.CookieJarIsNullException;
import com.iliauni.usersyncglobalservice.exception.NoRecordOfCookieJarException;
import com.iliauni.usersyncglobalservice.model.Cookie;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.repository.CookieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CookieService<T extends IpaClient> {
    CookieRepository repository;
    IpaCookieJarRetriever<T> cookieJarRetriever;


    @Autowired
    public CookieService(CookieRepository repository, IpaCookieJarRetriever<T> cookieJarRetriever) {
        this.repository = repository;
        this.cookieJarRetriever = cookieJarRetriever;
    }

    public Cookie generateAndSave(T client, String endpointUrl) {
        Cookie cookie = new Cookie();
        cookie.setCookie(generateCookie(client, endpointUrl));
        cookie.setCreationDate(new Date());

        return save(Optional.of(cookie));
    }

    private String generateCookie(T client, String endpointUrl) {
        return cookieJarRetriever.retrieveCookieJar(client, endpointUrl);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void deleteByDateBetween(
            Date creationDateFrom,
            Date creationDateTo) {
        repository.deleteCookieJarByCreationDateBetween(
                creationDateFrom,
                creationDateTo);
    }

    public Cookie findLatest() {
        try {
            return repository.findFirstByOrderByCreationDateDesc()
                    .orElseThrow(() -> new CookieJarIsNullException("Cookie jar is null"));
        } catch (CookieJarIsNullException exception) {
            throw new NoRecordOfCookieJarException(
                    "There is no record of cookie jar in the database: " + exception.getMessage()
            );
        }
    }

    public Cookie findByCreationDate(Date creationDate) {
        try {
            return repository.findCookieJarByCreationDate(creationDate)
                    .orElseThrow(() -> new CookieJarIsNullException("Cookie jar is null"));
        } catch (CookieJarIsNullException exception) {
            throw new NoRecordOfCookieJarException(
                    "There is no record of cookie jar with creation date " +
                            creationDate + " in the database: " + exception.getMessage()
            );
        }
    }

    private Cookie save(Optional<Cookie> optionalCookieJar) {
        return optionalCookieJar.map(cookieJar -> repository.save(cookieJar))
                .orElseThrow(() -> new CookieJarIsNullException("Cookie jar is null"));
    }
}
