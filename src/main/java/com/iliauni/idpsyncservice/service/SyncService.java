package com.iliauni.idpsyncservice.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Interface defining the operations for synchronizing entities with the database and identity providers (IDPs).
 *
 * @param <T> The type of entity to be synchronized.
 */
public interface SyncService<T> {

    /**
     * Synchronizes the given list of entities with the database and all clients.
     *
     * @param entities List of entities to synchronize.
     */
    void sync(List<T> entities);

    /**
     * Asynchronously retrieves and synchronizes entities from all clients to the database.
     *
     * @return CompletableFuture representing the asynchronous operations.
     */
    CompletableFuture<Void> syncFromAllIdps();
}
