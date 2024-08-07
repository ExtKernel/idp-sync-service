package com.iliauni.idpsyncservice.controller;

import com.iliauni.idpsyncservice.exception.ClientIsNullException;
import com.iliauni.idpsyncservice.exception.ClientNotFoundException;
import com.iliauni.idpsyncservice.exception.NoRecordOfClientsException;
import com.iliauni.idpsyncservice.model.SyncKcClient;
import com.iliauni.idpsyncservice.service.Oauth2ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/secured/client/KC")
@RestController
public class SyncKcClientController {
    private final Oauth2ClientService<SyncKcClient> clientService;

    @Autowired
    public SyncKcClientController(Oauth2ClientService<SyncKcClient> clientService) {
        this.clientService = clientService;
    }

    @PostMapping()
    public SyncKcClient save(@RequestBody SyncKcClient client) throws ClientIsNullException {
        return clientService.save(Optional.ofNullable(client));
    }

    @GetMapping()
    public List<SyncKcClient> findAll() throws NoRecordOfClientsException {
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public SyncKcClient findById(@PathVariable String id) throws ClientNotFoundException {
        return clientService.findById(id);
    }

    @PutMapping()
    public SyncKcClient update(@RequestBody SyncKcClient client) throws ClientIsNullException {
        return clientService.update(Optional.ofNullable(client));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        clientService.deleteById(id);
    }
}
