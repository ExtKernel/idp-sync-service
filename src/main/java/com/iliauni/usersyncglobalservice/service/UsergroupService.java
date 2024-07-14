package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.model.Usergroup;
import com.iliauni.usersyncglobalservice.repository.UsergroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsergroupService extends GenericCrudService<Usergroup, String> {

    @Autowired
    public UsergroupService(UsergroupRepository repository) {
        super(repository);
    }
}
