package com.iliauni.idpsyncservice.service;

import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends GenericCrudService<User, String>{

    @Autowired
    public UserService(UserRepository repository) {
        super(repository);
    }
}
