package com.iliauni.idpsyncservice.service;

import com.iliauni.idpsyncservice.exception.ClientIsNullException;
import com.iliauni.idpsyncservice.exception.ClientNotFoundException;
import com.iliauni.idpsyncservice.model.Cookie;
import com.iliauni.idpsyncservice.model.IpaClient;
import com.iliauni.idpsyncservice.repository.IpaClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IpaClientService extends GenericCrudService<IpaClient, String>
        implements CookieClientService<IpaClient> {
    private final CookieService<IpaClient> cookieService;
    private final CacheService cacheService;

    @Autowired
    public IpaClientService(
            IpaClientRepository repository,
            CookieService<IpaClient> cookieService,
            CacheService cacheService
    ) {
        super(repository);
        this.cookieService = cookieService;
        this.cacheService = cacheService;
    }

    @Override
    public Cookie generateAndSaveCookieJar(
            String clientId,
            String endpointUrl
    ) throws ClientNotFoundException, ClientIsNullException {
        IpaClient client = findById(clientId);
        List<Cookie> cookies;

        if (client.getCookies() != null) {
            cookies = client.getCookies();
        } else {
            cookies = new ArrayList<>();
        }

        Cookie cookie = cookieService.generateAndSave(
                client,
                endpointUrl
        );
        cookies.add(cookie);

        client.setCookies(cookies);
        save(Optional.of(client));

        return cookie;
    }

    @Override
    public void clearClientUserCache(IpaClient client) {
        cacheService.clearCacheByName("ipaUsers");
    }

    @Override
    public void clearClientUsergroupsCache(IpaClient client) {
        cacheService.clearCacheByName("ipaUsergroups");
    }
}
