package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.exception.ClientIsNullException;
import com.iliauni.usersyncglobalservice.exception.ClientNotFoundException;
import com.iliauni.usersyncglobalservice.exception.NoRecordOfClientsException;
import com.iliauni.usersyncglobalservice.model.Client;
import com.iliauni.usersyncglobalservice.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService<T extends Client> {
    private final ClientRepository<T> repository;

    @Autowired
    public ClientService(ClientRepository<T> repository) {
        this.repository = repository;
    }

    public T save(Optional<T> optionalClient) throws ClientIsNullException {
        return optionalClient.map(repository::save)
                .orElseThrow(() -> new ClientIsNullException("The Keycloak client is null"));
    }

    public T update(Optional<T> optionalClient) throws ClientIsNullException {
        return optionalClient.map(repository::save)
                .orElseThrow(() -> new ClientIsNullException("The Keycloak client is null"));
    }

    public void deleteById(String clientId) {
        repository.deleteClientById(findById(clientId).getId());
    }

    public T findById(String clientId) throws ClientNotFoundException {
        return repository.findClientById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Keycloak client with id " + clientId + " was not found"));
    }

    public List<T> findAll() throws NoRecordOfClientsException {
        if (!repository.findAll().isEmpty()) {
            return repository.findAll();
        } else {
            throw new NoRecordOfClientsException("There is no record of clients in the database");
        }
    }
}
