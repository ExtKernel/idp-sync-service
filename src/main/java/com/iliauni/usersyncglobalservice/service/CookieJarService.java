package com.iliauni.usersyncglobalservice.service;

import java.util.Date;
import java.util.Optional;

import com.iliauni.usersyncglobalservice.cookiejar.IpaCookieJarRetriever;
import com.iliauni.usersyncglobalservice.exception.CookieJarIsNullException;
import com.iliauni.usersyncglobalservice.exception.NoRecordOfCookieJarException;
import com.iliauni.usersyncglobalservice.model.CookieJar;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.repository.CookieJarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CookieJarService<T extends IpaClient> {
    CookieJarRepository repository;
    IpaCookieJarRetriever<T> cookieJarRetriever;


    @Autowired
    public CookieJarService(CookieJarRepository repository, IpaCookieJarRetriever<T> cookieJarRetriever) {
        this.repository = repository;
        this.cookieJarRetriever = cookieJarRetriever;
    }

    public CookieJar generateAndSave(T client) {
        CookieJar cookieJar = new CookieJar();
        cookieJar.setCookie(generateCookie(client));
        cookieJar.setCreationDate(new Date());

        return save(Optional.of(cookieJar));
    }

    private String generateCookie(T client) {
        return cookieJarRetriever.retrieveCookieJar(client);
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

    public CookieJar findLatest() {
        try {
            return repository.findFirstByOrderByCreationDateDesc()
                    .orElseThrow(() -> new CookieJarIsNullException("Cookie jar is null"));
        } catch (CookieJarIsNullException exception) {
            throw new NoRecordOfCookieJarException(
                    "There is no record of cookie jar in the database: " + exception.getMessage()
            );
        }
    }

    public CookieJar findByCreationDate(Date creationDate) {
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

    private CookieJar save(Optional<CookieJar> optionalCookieJar) {
        return optionalCookieJar.map(cookieJar -> repository.save(cookieJar))
                .orElseThrow(() -> new CookieJarIsNullException("Cookie jar is null"));
    }
}
