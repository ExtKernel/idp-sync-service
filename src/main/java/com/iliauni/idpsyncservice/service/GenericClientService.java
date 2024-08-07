package com.iliauni.idpsyncservice.service;

import com.iliauni.idpsyncservice.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class GenericClientService<T extends Client>
        extends GenericCrudService<T, String>
        implements ClientService<T> {

    private final CacheService cacheService;

    protected GenericClientService(
            JpaRepository<T, String> repository,
            CacheService cacheService
    ) {
        super(repository);
        this.cacheService = cacheService;
    }

    /**
     * Makes the client's class name lowercase,
     * then removes "client" from the client class name and adds "Users" to the end.
     * This is done according to the convention for cache names for this app.
     * The final result matches a pattern: "clientclassnameUsers".
     *
     * @param client the client whose cache should be cleared.
     */
    @Override
    public void clearClientUserCache(T client) {
        cacheService.clearCacheByName(
                client
                        .getClass()
                        .getSimpleName()
                        .toLowerCase()
                        .split("client")[0] + "Users"
        );
    }

    /**
     * Makes the client's class name lowercase,
     * then removes "client" from the client class name and adds "Usergroups" to the end.
     * This is done according to the convention for cache names for this app.
     * The final result matches a pattern: "clientclassnameUsergroups".
     *
     * @param client the client whose cache should be cleared.
     */
    @Override
    public void clearClientUsergroupsCache(T client) {
        cacheService.clearCacheByName(
                client
                        .getClass()
                        .getSimpleName()
                        .toLowerCase()
                        .split("client")[0] + "Usergroups"
        );
    }
}
