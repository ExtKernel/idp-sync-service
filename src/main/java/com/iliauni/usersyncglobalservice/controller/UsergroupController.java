package com.iliauni.usersyncglobalservice.controller;

import com.iliauni.usersyncglobalservice.model.Usergroup;
import com.iliauni.usersyncglobalservice.service.UsergroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        return service.findById(usergroupName);
    }

    @GetMapping()
    public List<Usergroup> findAll() {
        return service.findAll();
    }

    @PutMapping()
    public Usergroup update(@RequestBody Usergroup usergroup) {
        return service.update(Optional.ofNullable(usergroup));
    }

    @DeleteMapping("/{usergroupName}")
    public void delete(@PathVariable String usergroupName) {
        service.deleteById(usergroupName);
    }
}
