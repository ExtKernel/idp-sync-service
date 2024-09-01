package com.iliauni.idpsyncservice.service;

import com.iliauni.idpsyncservice.cookie.IpaCookieRetriever;
import com.iliauni.idpsyncservice.exception.CookieJarIsNullException;
import com.iliauni.idpsyncservice.model.Cookie;
import com.iliauni.idpsyncservice.model.IpaClient;
import com.iliauni.idpsyncservice.repository.CookieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class CookieService<T extends IpaClient> extends GenericCrudService<Cookie, Long> {
    CookieRepository repository;
    IpaCookieRetriever<T> cookieJarRetriever;


    @Autowired
    public CookieService(CookieRepository repository, IpaCookieRetriever<T> cookieJarRetriever) {
        super(repository);
        this.repository = repository;
        this.cookieJarRetriever = cookieJarRetriever;
    }

    public Cookie generateAndSave(T client, String endpointUrl) {
        Cookie cookie = new Cookie();
        cookie.setCookie(generateCookie(client, endpointUrl));
        cookie.setCreationDate(new Date());

        try {
            return save(Optional.of(cookie));
        } catch (CookieJarIsNullException e) {
            throw new CookieJarIsNullException("Cookie jar is null");
        }
    }

    private String generateCookie(T client, String endpointUrl) {
        return cookieJarRetriever.retrieveCookieJar(client, endpointUrl);
    }
}
