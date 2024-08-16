package com.iliauni.idpsyncservice.service;

import com.iliauni.idpsyncservice.exception.ModelNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * An interface for a generic CRUD {@link org.springframework.stereotype.Service}.
 *
 * @param <T> the type of object on which CRUD operations will be performed.
 * @param <ID> the type of the id the object has.
 */
public interface CrudService<T, ID> {

    /**
     * Saves a {@link T}.
     *
     * @param optionalT an {@link Optional} of the {@link T}.
     * @return the {@link T}.
     */
    T save(Optional<T> optionalT);

    /**
     * Finds all {@link T} objects.
     *
     * @return all {@link T} objects, that exist in the database.
     */
    List<T> findAll();

    /**
     * Finds a {@link T} by an id.
     *
     * @param id the id of the {@link T}.
     * @return the {@link T}, found by the id;
     * @throws ModelNotFoundException - if a model with the given id was not found.
     */
    T findById(ID id);

    /**
     * Saves a {@link T}, using a JPA repository.
     *
     * @param optionalT an {@link Optional} of the {@link T}.
     * @return the updated {@link T}.
     */
    T update(Optional<T> optionalT);

    void deleteById(ID id);
}
