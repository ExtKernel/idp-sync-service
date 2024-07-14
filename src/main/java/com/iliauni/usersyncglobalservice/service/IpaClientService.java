package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.exception.ClientIsNullException;
import com.iliauni.usersyncglobalservice.exception.ClientNotFoundException;
import com.iliauni.usersyncglobalservice.model.Cookie;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.repository.IpaClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IpaClientService extends GenericCrudService<IpaClient, String> implements CookieClientService<IpaClient> {
    private final CookieService<IpaClient> cookieService;

    @Autowired
    public IpaClientService(
            IpaClientRepository repository,
            CookieService<IpaClient> cookieService
    ) {
        super(repository);
        this.cookieService = cookieService;
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
}
