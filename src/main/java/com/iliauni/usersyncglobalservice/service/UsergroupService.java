package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.exception.NoRecordOfUsergroupsException;
import com.iliauni.usersyncglobalservice.exception.UsergroupIsNullException;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import com.iliauni.usersyncglobalservice.repository.UsergroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsergroupService {
    private final UsergroupRepository repository;

    @Autowired
    public UsergroupService(UsergroupRepository repository) {
        this.repository = repository;
    }

    public Usergroup save(Optional<Usergroup> optionalUsergroup) throws UsergroupIsNullException {
        return optionalUsergroup.map(repository::save)
                .orElseThrow(() -> new UsergroupIsNullException("User group is not present"));
    }

    public Usergroup findByName(String usergroupName) throws UsergroupIsNullException {
        return repository.findUsergroupByName(usergroupName)
                .orElseThrow(() -> new UsergroupIsNullException("User group " + usergroupName + " was not found"));
    }

    public List<Usergroup> findAll() throws NoRecordOfUsergroupsException {
        if (!repository.findAll().isEmpty()) {
            return repository.findAll();
        } else {
            throw new NoRecordOfUsergroupsException("There is no record of user groups in the database");
        }
    }

    public Usergroup update(Optional<Usergroup> optionalUsergroup) throws UsergroupIsNullException {
        return optionalUsergroup.map(repository::save)
                .orElseThrow(() -> new UsergroupIsNullException("User group is not present"));
    }

    public void deleteByName(String usergroupName) {
        repository.deleteUsergroupByName(usergroupName);
    }
}
