package com.iliauni.idpsyncservice.controller;

import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/secured/user")
@RestController
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping()
    public User save(@RequestBody User user) {
        return service.save(Optional.ofNullable(user));
    }

    @GetMapping("/{username}")
    public User findByName(@PathVariable String username) {
        return service.findById(username);
    }

    @GetMapping()
    public List<User> findAll() {
        return service.findAll();
    }

    @PutMapping()
    public User update(@RequestBody User user) {
        return service.update(Optional.ofNullable(user));
    }

    @PatchMapping("/{username}")
    public String updatePassword(
            @PathVariable String username,
            @RequestParam String newPassword,
            @RequestParam(required = false) boolean sync,
            @RequestParam(required = false) boolean mail
    ) {
        if (sync && mail) {
            return null; // create a method for that
        } else if (sync) {
            return service.updatePassword(
                    username,
                    newPassword,
                    sync
            );
        } else if (mail) {
            return null; // create a method for that
        } else {
            return service.updatePassword(
                    username,
                    newPassword
            );
        }
    }

    @DeleteMapping("/{username}")
    public void delete(@PathVariable String username) {
        service.deleteById(username);
    }
}
