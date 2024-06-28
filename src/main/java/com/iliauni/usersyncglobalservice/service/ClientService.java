package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.exception.ClientIsNullException;
import com.iliauni.usersyncglobalservice.exception.ClientNotFoundException;
import com.iliauni.usersyncglobalservice.exception.NoRecordOfClientsException;
import com.iliauni.usersyncglobalservice.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService<T extends Client> {

    /**
     * Saves a {@link T}.
     *
     * @param optionalClient an {@link Optional} of the {@link T}.
     * @return the {@link T}.
     * @throws ClientIsNullException if the given {@link T} is null.
     */
    T save(Optional<T> optionalClient);

    /**
     * Finds all {@link T} objects.
     *
     * @return all {@link T} objects, that exist in the database.
     * @throws NoRecordOfClientsException if there is no record of any {@link T}
     *                                    objects in the database.
     */
    List<T> findAll();

    /**
     * Finds a {@link T} by an id.
     *
     * @param id the id of the {@link T}.
     * @return the {@link T}, found by the id;
     * @throws ClientNotFoundException if a {@link T} with the id was not found.
     */
    T findById(String id);

    /**
     * Saves a {@link T}, using a JPA repository.
     *
     * @param optionalClient an {@link Optional} of the {@link T}.
     * @return the updated {@link T}.
     * @throws ClientIsNullException if the given {@link T} is null.
     */
    T update(Optional<T> optionalClient);

    void deleteById(String id);
}
