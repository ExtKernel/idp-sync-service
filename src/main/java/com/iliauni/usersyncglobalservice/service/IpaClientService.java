package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.model.CookieJar;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IpaClientService<T extends IpaClient> extends ClientService<T> {
    @Lazy
    private final CookieJarService<T> cookieJarService;

    @Autowired
    public IpaClientService(ClientRepository<T> repository, CookieJarService<T> cookieJarService) {
        super(repository);
        this.cookieJarService = cookieJarService;
    }

    public CookieJar generateAndSaveCookieJar(String clientId) {
        T client = findById(clientId);
        List<CookieJar> cookieJars;

        if (client.getCookieJars() != null) {
            cookieJars = client.getCookieJars();
        } else {
            cookieJars = new ArrayList<>();
        }

        CookieJar cookieJar = cookieJarService.generateAndSave(client);
        cookieJars.add(cookieJar);

        client.setCookieJars(cookieJars);
        save(Optional.of(client));

        return cookieJar;
    }
}
