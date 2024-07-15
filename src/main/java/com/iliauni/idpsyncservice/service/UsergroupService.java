package com.iliauni.idpsyncservice.service;

import com.iliauni.idpsyncservice.model.Usergroup;
import com.iliauni.idpsyncservice.repository.UsergroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsergroupService extends GenericCrudService<Usergroup, String> {

    @Autowired
    public UsergroupService(UsergroupRepository repository) {
        super(repository);
    }
}
