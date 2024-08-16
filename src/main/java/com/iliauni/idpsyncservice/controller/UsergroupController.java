package com.iliauni.idpsyncservice.controller;

import com.iliauni.idpsyncservice.model.Usergroup;
import com.iliauni.idpsyncservice.service.UsergroupService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/secured/group")
@RestController
public class UsergroupController {
    private final UsergroupService service;

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
