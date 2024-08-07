package com.iliauni.idpsyncservice.service;

import com.iliauni.idpsyncservice.model.Client;

public interface ClientService<T extends Client> extends CrudService<T, String> {
    void clearClientUserCache(T client);
    void clearClientUsergroupsCache(T client);
}
