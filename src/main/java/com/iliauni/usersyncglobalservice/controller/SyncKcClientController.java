package com.iliauni.usersyncglobalservice.controller;

import com.iliauni.usersyncglobalservice.model.SyncKcClient;
import com.iliauni.usersyncglobalservice.service.Oauth2ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/secured/client/KC")
@RestController
public class SyncKcClientController {
    Oauth2ClientService<SyncKcClient> clientService;

    @Autowired
    public SyncKcClientController(Oauth2ClientService<SyncKcClient> clientService) {
        this.clientService = clientService;
    }

    @PostMapping()
    public SyncKcClient save(@RequestBody SyncKcClient client) {
        return clientService.save(Optional.ofNullable(client));
    }

    @GetMapping()
    public List<SyncKcClient> findAll() {
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public SyncKcClient findById(@PathVariable String id) {
        return clientService.findById(id);
    }

    @PutMapping()
    public SyncKcClient update(@RequestBody SyncKcClient client) {
        return clientService.update(Optional.ofNullable(client));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        clientService.deleteById(id);
    }
}
