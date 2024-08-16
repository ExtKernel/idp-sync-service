package com.iliauni.idpsyncservice.service;

import com.iliauni.idpsyncservice.exception.CacheDoesNotExistException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    private final CacheManager cacheManager;

    @Autowired
    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void clearCacheByName(String cacheName) {
        try {
            Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
        } catch (NullPointerException exception) {
            throw new CacheDoesNotExistException(
                    "A cache with name "
                            + cacheName
                            + " doesn't exist",
                    exception
            );
        }
    }
}
