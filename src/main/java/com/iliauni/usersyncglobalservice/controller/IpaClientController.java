package com.iliauni.usersyncglobalservice.controller;

import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.service.CookieClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/secured/client/IPA")
@RestController
public class IpaClientController {
    CookieClientService<IpaClient> clientService;

    @Autowired
    public IpaClientController(CookieClientService<IpaClient> clientService) {
        this.clientService = clientService;
    }

    @PostMapping()
    public IpaClient save(@RequestBody IpaClient client) {
        return clientService.save(Optional.ofNullable(client));
    }

    @GetMapping()
    public List<IpaClient> findAll() {
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public IpaClient findById(@PathVariable String id) {
        return clientService.findById(id);
    }

    @PutMapping()
    public IpaClient update(@RequestBody IpaClient client) {
        return clientService.update(Optional.ofNullable(client));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        clientService.deleteById(id);
    }
}
