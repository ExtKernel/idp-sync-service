package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.exception.ClientIsNullException;
import com.iliauni.usersyncglobalservice.exception.ClientNotFoundException;
import com.iliauni.usersyncglobalservice.exception.NoRecordOfClientsException;
import com.iliauni.usersyncglobalservice.model.Cookie;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.repository.IpaClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IpaClientService implements CookieClientService<IpaClient> {
    private final IpaClientRepository repository;
    private final CookieService<IpaClient> cookieService;

    @Autowired
    public IpaClientService(
            IpaClientRepository repository,
            CookieService<IpaClient> cookieService
    ) {
        this.repository = repository;
        this.cookieService = cookieService;
    }

    @Override
    public Cookie generateAndSaveCookieJar(String clientId, String endpointUrl) throws ClientNotFoundException, ClientIsNullException {
        IpaClient client = findById(clientId);
        List<Cookie> cookies;

        if (client.getCookies() != null) {
            cookies = client.getCookies();
        } else {
            cookies = new ArrayList<>();
        }

        Cookie cookie = cookieService.generateAndSave(client, endpointUrl);
        cookies.add(cookie);

        client.setCookies(cookies);
        save(Optional.of(client));

        return cookie;
    }

    @Transactional
    @Override
    public IpaClient save(Optional<IpaClient> optionalClient) throws ClientIsNullException {
        return optionalClient.map(repository::save)
                .orElseThrow(() -> new ClientIsNullException("FreeIPA client is null"));
    }

    @Override
    public List<IpaClient> findAll() throws NoRecordOfClientsException {
        List<IpaClient> clients = repository.findAll();

        if (!clients.isEmpty()) {
            return clients;
        } else {
            throw new NoRecordOfClientsException("There is no record of clients in the database");
        }
    }

    @Override
    public IpaClient findById(String id) throws ClientNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with id " + id + " was not found"));
    }

    @Override
    public IpaClient update(Optional<IpaClient> optionalClient) throws ClientIsNullException {
        return optionalClient.map(repository::save)
                .orElseThrow(() -> new ClientIsNullException("FreeIPA client is null"));
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
