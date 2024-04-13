package com.iliauni.usersyncglobalservice.controller;

import com.iliauni.usersyncglobalservice.model.Usergroup;
import com.iliauni.usersyncglobalservice.service.UsergroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequestMapping("/secured/group")
@RestController
public class UsergroupController {
    UsergroupService service;

    @Autowired
    public UsergroupController(UsergroupService service) {
        this.service = service;
    }

    @PostMapping()
    public Usergroup save(@RequestBody Usergroup usergroup) {
        return service.save(Optional.ofNullable(usergroup));
    }

    @GetMapping("/{usergroupName}")
    public Usergroup findByName(@PathVariable String usergroupName) {
        return service.findByName(usergroupName);
    }

    @PutMapping()
    public Usergroup update(@RequestBody Usergroup usergroup) {
        return service.update(Optional.ofNullable(usergroup));
    }

    @DeleteMapping("/{usergroupName}")
    public void delete(@PathVariable String usergroupName) {
        service.deleteByName(usergroupName);
    }
}
