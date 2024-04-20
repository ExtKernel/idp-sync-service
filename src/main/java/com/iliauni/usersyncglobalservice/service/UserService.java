package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.exception.NoRecordOfUsersException;
import com.iliauni.usersyncglobalservice.exception.UserIsNullException;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public User save(Optional<User> optionalUser) throws UserIsNullException {
        return optionalUser.map(repository::save)
                .orElseThrow(() -> new UserIsNullException("User is not present"));
    }

    public User findByName(String username) throws UserIsNullException {
        return repository.findUserByUsername(username)
                .orElseThrow(() -> new UserIsNullException("User " + username + " was not found"));
    }

    public List<User> findAll() throws NoRecordOfUsersException {
        if (!repository.findAll().isEmpty()) {
            return repository.findAll();
        } else {
            throw new NoRecordOfUsersException("There is no record of users in the database");
        }
    }

    @Transactional
    public User update(Optional<User> optionalUser) throws UserIsNullException {
        return optionalUser.map(repository::save)
                .orElseThrow(() -> new UserIsNullException("User group is not present"));
    }

    @Transactional
    public void deleteByUsername(String username) {
        repository.deleteUserByUsername(username);
    }
}
