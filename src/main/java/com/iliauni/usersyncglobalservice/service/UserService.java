package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends GenericCrudService<User, String>{

    @Autowired
    public UserService(UserRepository repository) {
        super(repository);
    }
}
