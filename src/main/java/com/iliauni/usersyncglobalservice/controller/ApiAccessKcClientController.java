package com.iliauni.usersyncglobalservice.controller;

import com.iliauni.usersyncglobalservice.exception.ClientIsNullException;
import com.iliauni.usersyncglobalservice.exception.ClientNotFoundException;
import com.iliauni.usersyncglobalservice.exception.NoRecordOfClientsException;
import com.iliauni.usersyncglobalservice.model.ApiAccessKcClient;
import com.iliauni.usersyncglobalservice.service.Oauth2ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/secured/client/API/KC")
@RestController
public class ApiAccessKcClientController {
    Oauth2ClientService<ApiAccessKcClient> clientService;

    @Autowired
    public ApiAccessKcClientController(Oauth2ClientService<ApiAccessKcClient> clientService) {
        this.clientService = clientService;
    }

    @PostMapping()
    public ApiAccessKcClient save(@RequestBody ApiAccessKcClient client) {
        try {
            return clientService.save(Optional.ofNullable(client));
        } catch (ClientIsNullException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping()
    public List<ApiAccessKcClient> findAll() {
        try {
            return clientService.findAll();
        } catch (NoRecordOfClientsException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ApiAccessKcClient findById(@PathVariable String id) {
        try {
            return clientService.findById(id);
        } catch (ClientNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping()
    public ApiAccessKcClient update(@RequestBody ApiAccessKcClient client) throws ClientIsNullException {
        return clientService.update(Optional.ofNullable(client));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        clientService.deleteById(id);
    }
}
