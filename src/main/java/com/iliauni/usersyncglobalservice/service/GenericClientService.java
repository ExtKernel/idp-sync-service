package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class GenericClientService<T extends Client>
        extends GenericCrudService<T, String>
        implements ClientService<T> {

    protected GenericClientService(JpaRepository<T, String> repository) {
        super(repository);
    }
}
