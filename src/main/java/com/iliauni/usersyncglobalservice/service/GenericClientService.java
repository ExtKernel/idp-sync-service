package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.exception.ClientIsNullException;
import com.iliauni.usersyncglobalservice.exception.ClientNotFoundException;
import com.iliauni.usersyncglobalservice.exception.NoRecordOfClientsException;
import com.iliauni.usersyncglobalservice.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public class GenericClientService<T extends Client> implements ClientService<T> {
    JpaRepository<T, String> repository;

    public GenericClientService(JpaRepository<T, String> repository) {
        this.repository = repository;
    }

    @Override
    public T save(Optional<T> optionalClient) {
        return optionalClient.map(repository::save)
                .orElseThrow(() -> new ClientIsNullException("The client is null"));
    }

    @Override
    public List<T> findAll() {
        List<T> clients = repository.findAll();

        if (!clients.isEmpty()) {
            return clients;
        } else {
            throw new NoRecordOfClientsException("There is no record of the given type of clients in the database");
        }
    }

    @Override
    public T findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(
                        "A client with id "
                                + id
                                + " was not found"
                ));
    }

    @Override
    public T update(Optional<T> optionalClient) {
        return optionalClient.map(client -> repository.save(client))
                .orElseThrow(() -> new ClientIsNullException("The client is null"));
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
